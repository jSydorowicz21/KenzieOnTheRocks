package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.NonCachingDrinkDao;
import com.kenzie.capstone.service.model.Drink;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DrinkService {

    private NonCachingDrinkDao nonCachingDrinkDao;

    @Inject
    public DrinkService(NonCachingDrinkDao nonCachingDrinkDao) {
        this.nonCachingDrinkDao = nonCachingDrinkDao;
    }

    public Drink getDrink(String id) {
        DrinkRecord drinkRecord = nonCachingDrinkDao.getDrink(id);

        if (drinkRecord == null){
            throw new IllegalArgumentException("Drink not found");
        }

        return new Drink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId());

    }

    public Drink addDrink(Drink drink) {

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drink.getId());
        drinkRecord.setName(drink.getName());
        drinkRecord.setIngredients(drink.getIngredients());
        drinkRecord.setUserId(drink.getUserId());

        nonCachingDrinkDao.addDrink(drinkRecord);

        return drink;
    }

    public Drink updateDrink(Drink drink) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drink.getId());
        drinkRecord.setName(drink.getName());
        drinkRecord.setIngredients(drink.getIngredients());
        drinkRecord.setUserId(drink.getUserId());

        nonCachingDrinkDao.updateDrink(drinkRecord);

        return drink;
    }

    public void deleteDrink(String id) {

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);

        nonCachingDrinkDao.deleteDrink(drinkRecord);

    }

    public List<Drink> getAllDrinks() {
        return nonCachingDrinkDao.getAllDrinks().stream()
                .map(drinkRecord ->
                    new Drink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId()))
                .collect(Collectors.toList());
    }

    public List<Drink> getDrinksByUserId(String userId) {
        return nonCachingDrinkDao.getDrinksByUserId(userId).stream()
                .map(drinkRecord ->
                        new Drink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId()))
                .collect(Collectors.toList());
    }

}
