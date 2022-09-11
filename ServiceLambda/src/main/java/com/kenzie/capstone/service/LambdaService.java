package com.kenzie.capstone.service;

import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.DrinkDao;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private DrinkDao drinkDao;

    @Inject
    public LambdaService(DrinkDao drinkDao) {
        this.drinkDao = drinkDao;
    }

    public ExampleData getExampleData(String id) {
        List<DrinkRecord> records = drinkDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        DrinkRecord record = drinkDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }
}
