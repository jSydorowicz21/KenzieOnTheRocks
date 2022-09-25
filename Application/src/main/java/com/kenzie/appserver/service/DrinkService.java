package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.model.Drink;

import com.kenzie.appserver.service.model.UserHasExistingDrinkException;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.DrinkData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DrinkService {
    private DrinkRepository drinkRepository;
    private LambdaServiceClient lambdaServiceClient;

    public DrinkService(DrinkRepository drinkRepository, LambdaServiceClient lambdaServiceClient) {
        this.drinkRepository = drinkRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Drink findById(String id) {

        // Example getting data from the lambda
        DrinkData dataFromLambda = lambdaServiceClient.getExampleData(id);

        // Example getting data from the local repository
        Drink dataFromDynamo = drinkRepository
                .findById(id)
                .map(example -> new Drink(example.getId(), example.getName()))
                .orElse(null);

        return dataFromDynamo;
    }

//    public Drink addNewExample(String name) {
//        // Example sending data to the lambda
//        DrinkData dataFromLambda = lambdaServiceClient.setExampleData(name);
//
//        // Example sending data to the local repository
//        DrinkRecord drinkRecord = new DrinkRecord();
//        drinkRecord.setId(dataFromLambda.getId());
//        drinkRecord.setName(dataFromLambda.getData());
//        drinkRepository.save(drinkRecord);
//
//        Drink drink = new Drink(dataFromLambda.getId(), name);
//        return drink;
//    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        for (DrinkRecord record : drinkRepository.findAll()) {
            if (record != null) {
                drinks.add(convertRecordToDrink(record));
            }
        }
        return drinks;
    }

    //add drink
    public Drink addDrink(DrinkCreateRequest request) {

        if (request == null || request.getName() == null || request.getIngredients().isEmpty() || request.getIngredients() == null){
            throw new IllegalArgumentException("BROKEN");
        }

        //check if this drink already present
        if (drinkRepository.UserHasExistingDrink(request.getName())) {
            throw new UserHasExistingDrinkException(request.getName() + "has an existing drink. The existing drink will be updated");
        }

        DrinkRecord record = createRecordFromRequest(request);

        drinkRepository.save(record);


        return convertRecordToDrink(record);
    }



    public Drink updateDrink(String userID, Drink drink) {

        // Add null checks

        Drink drinkFromDB = findById(drink.getId());


        DrinkRecord record = new DrinkRecord(drinkFromDB.getId(), drink.getName(), userID);
        record.setIngredients(drink.getIngredients());

        drinkRepository.save(record);


        return drink;

    }

    public void delete(Drink drink){
        try{
            drinkRepository.delete(createRecordFromDrink(drink));

        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is no matching drink");
        }
    }
    private DrinkRecord createRecordFromRequest(DrinkCreateRequest request) {
        DrinkRecord record = new DrinkRecord(request.getName(), UUID.randomUUID().toString(), request.getUserId());
        record.setIngredients(request.getIngredients());
        return record;
    }

    private DrinkRecord createRecordFromDrink(Drink drink) {
        DrinkRecord record = new DrinkRecord(drink.getId(), drink.getName(), drink.getId());
        record.setId(drink.getId());
        record.setName(drink.getName());
        record.setIngredients(drink.getIngredients());
        return record;


    }


    private Drink convertRecordToDrink(DrinkRecord record) {
        Drink drink = new Drink(record.getId(), record.getName());
        drink.setUserId(record.getUserId());
        drink.setIngredients(record.getIngredients());
        return drink;
    }
}
