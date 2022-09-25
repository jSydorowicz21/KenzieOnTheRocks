package com.kenzie.capstone.service;

import com.kenzie.capstone.service.model.DrinkData;
import com.kenzie.capstone.service.dao.DrinkDao;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class DrinkService {

    private DrinkDao drinkDao;

    @Inject
    public DrinkService(DrinkDao drinkDao) {
        this.drinkDao = drinkDao;
    }

    public DrinkData getExampleData(String id) {
        List<DrinkRecord> records = drinkDao.getExampleData(id);
        if (records.size() > 0) {
            return new DrinkData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public DrinkData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        DrinkRecord record = drinkDao.setExampleData(id, data);
        return new DrinkData(id, data);
    }
}
