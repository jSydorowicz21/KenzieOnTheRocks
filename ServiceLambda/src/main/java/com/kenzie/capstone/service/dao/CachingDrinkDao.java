package com.kenzie.capstone.service.dao;

import com.google.gson.Gson;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CachingDrinkDao implements DrinkDao{

    private static final int DRINK_READ_TTL = 60 * 60;
    private static final String DRINK_KEY = "DrinkKey::%s";
    private CacheClient cacheClient;
    private DrinkDao drinkDao;

    private Gson gson = new Gson();

    @Inject
    public CachingDrinkDao(CacheClient cacheClient, NonCachingDrinkDao drinkDao) {
        this.cacheClient = cacheClient;
        this.drinkDao = drinkDao;
    }
    @Override
    public DrinkRecord addDrink(DrinkRecord drink) {
        cacheClient.invalidate(keyMaker(drink.getId()));
        return drinkDao.addDrink(drink);
    }

    @Override
    public DrinkRecord getDrink(String id) {
        if (cacheClient.getValue(keyMaker(id)) != null && cacheClient.getValue(keyMaker(id)).isPresent()) {
            return cacheToRecord(cacheClient.getValue(keyMaker(id)).get());
        }
        DrinkRecord drink = drinkDao.getDrink(id);
        if (drink != null) {
            addToCache(drink);
        }
        return drink;
    }

    @Override
    public DrinkRecord updateDrink(DrinkRecord drink) {
        cacheClient.invalidate(keyMaker(drink.getId()));
        return drinkDao.updateDrink(drink);
    }

    @Override
    public void deleteDrink(DrinkRecord drink) {
        cacheClient.invalidate(keyMaker(drink.getId()));
        drinkDao.deleteDrink(drink);
    }

    @Override
    public List<DrinkRecord> getAllDrinks() {

        Optional<List<String>> cacheRecords = cacheClient.getAll();

        if (cacheRecords != null && cacheRecords.isPresent()) {
            return cacheRecords.get().stream().filter(Objects::nonNull).map(this::cacheToRecord).collect(Collectors.toList());
        }

        return drinkDao.getAllDrinks();
    }

    @Override
    public List<DrinkRecord> getDrinksByUserId(String userId) {

        List<DrinkRecord> cachedDrinks = getAllDrinks();
        if (cachedDrinks != null) {
            return cachedDrinks.stream().filter(drink -> drink.getUserId().equals(userId)).collect(Collectors.toList());
        }

        return drinkDao.getDrinksByUserId(userId);
    }

    private DrinkRecord cacheToRecord(Object value) {
        return (DrinkRecord) value;
    }

    private String keyMaker(String id) {
        return String.format(DRINK_KEY, id);
    }

    private void addToCache(DrinkRecord drink) {
        cacheClient.setValue(keyMaker(drink.getId()), DRINK_READ_TTL,
                 gson.toJson(drink));
    }
}
