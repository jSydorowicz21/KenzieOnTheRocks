package com.kenzie.capstone.service;

import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;
import com.kenzie.capstone.service.dao.CachingDrinkDao;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.DrinkRequest;
import com.kenzie.capstone.service.model.LambdaDrink;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class DrinkService {

    private final CachingDrinkDao DrinkDao;

    @Inject
    public DrinkService(CachingDrinkDao DrinkDao) {
        this.DrinkDao = DrinkDao;
    }

    @ExcludeFromJacocoGeneratedReport
    public LambdaDrink getDrink(String id) {


        DrinkRecord drinkRecord = DrinkDao.getDrink(id);


        if (drinkRecord == null){
            return null;
        }

        return new LambdaDrink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId());

    }

    public LambdaDrink addDrink(DrinkRequest drinkRequest) {

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drinkRequest.getId());
        drinkRecord.setName(drinkRequest.getName());
        drinkRecord.setIngredients(drinkRequest.getIngredients());
        drinkRecord.setUserId(drinkRequest.getUserId());

        DrinkDao.addDrink(drinkRecord);
        return toDrink(drinkRecord);
    }

    public LambdaDrink updateDrink(LambdaDrink lambdaDrink) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(lambdaDrink.getId());
        drinkRecord.setName(lambdaDrink.getName());
        drinkRecord.setIngredients(lambdaDrink.getIngredients());
        drinkRecord.setUserId(lambdaDrink.getUserId());

        DrinkDao.updateDrink(drinkRecord);

        return lambdaDrink;
    }

    public String deleteDrink(String id) {

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);

        DrinkDao.deleteDrink(drinkRecord);

        return id;

    }

    public List<LambdaDrink> getAllDrinks() {
        return DrinkDao.getAllDrinks().stream()
                .map(drinkRecord ->
                    new LambdaDrink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId()))
                .collect(Collectors.toList());
    }

    public List<LambdaDrink> getDrinksByUserId(String userId) {
        return DrinkDao.getDrinksByUserId(userId).stream()
                .map(drinkRecord ->
                        new LambdaDrink(drinkRecord.getId(), drinkRecord.getName(), drinkRecord.getIngredients(), drinkRecord.getUserId()))
                .collect(Collectors.toList());
    }
    @ExcludeFromJacocoGeneratedReport
    private LambdaDrink toDrink(DrinkRecord record){
        LambdaDrink rec = new LambdaDrink();
        rec.setId(record.getId());
        rec.setName(record.getName());
        rec.setIngredients(record.getIngredients());
        rec.setUserId(record.getId());
        return rec;
    }
}
