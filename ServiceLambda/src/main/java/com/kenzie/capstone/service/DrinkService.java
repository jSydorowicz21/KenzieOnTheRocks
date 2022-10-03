package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.NonCachingDrinkDao;
import com.kenzie.capstone.service.model.Drink;
import com.kenzie.capstone.service.model.DrinkData;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class DrinkService {

    private NonCachingDrinkDao nonCachingDrinkDao;

    @Inject
    public DrinkService(NonCachingDrinkDao nonCachingDrinkDao) {
        this.nonCachingDrinkDao = nonCachingDrinkDao;
    }

    public Drink getExampleData(String id) {
        List<DrinkRecord> records = nonCachingDrinkDao.getExampleData(id);
        if (records.size() > 0) {
            return new Drink(records.get(0));
        }
        return null;
    }

    public DrinkData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        DrinkRecord record = nonCachingDrinkDao.setExampleData(id, data);
        return new DrinkData(id, data);
    }
}
