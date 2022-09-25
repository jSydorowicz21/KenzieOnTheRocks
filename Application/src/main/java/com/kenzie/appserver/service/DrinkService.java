package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.model.Drink;

import com.kenzie.appserver.service.model.UserHasExistingDrinkException;
import com.kenzie.appserver.service.model.UserHasNoExistingDrinkException;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.DrinkData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .map(example -> new Drink(example.getId(), example.getName(), example.getUserId()))
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
        Iterable<DrinkRecord> drinkIterator = drinkRepository.findAll();
        for (DrinkRecord record : drinkIterator) {
            if (record != null) {
                drinks.add(convertRecordToDrink(record));
            }
        }
        return drinks;
    }

    public Drink getDrink(String name, String userId){
        List<Drink> drinks = getAllDrinks().stream().filter(drink -> drink.getUserId().equals(userId)).collect(Collectors.toList());
        //return the drink or null
        return drinks.isEmpty() ? null : drinks.get(0);

    }

    //add drink
    public Drink addDrink(DrinkCreateRequest request) {
        drinkRepository.findById(request.getName());
        //check if this drink already present
        if (drinkRepository.UserHasExistingDrink(request.getName())) {
            throw new UserHasExistingDrinkException(request.getName() + "has an existing drink. The existing drink will be updated");
        }
        DrinkRecord record = createRecordFromRequest(request);
        drinkRepository.save(record);

        Drink drink = convertRecordToDrink(record);
        return drink;
    }



    public Drink updateDrink(DrinkUpdateRequest request) {
        drinkRepository.findById(request.getId());
        if(!drinkRepository.UserHasExistingDrink(request.getUserId())) {
            throw new UserHasNoExistingDrinkException(request.getUserId() + "does not have existing drink." + "New drink will be added");
        }
        DrinkRecord existingRecord = drinkRepository.findDrinkByUserId(request.getUserId());

        DrinkRecord updateRecord = new DrinkRecord(existingRecord.getId(), existingRecord.getName(), existingRecord.getUserId());
        updateRecord.setIngredients(request.getIngredients());
        drinkRepository.save(updateRecord);
        return convertRecordToDrink(updateRecord);



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
        DrinkRecord record = new DrinkRecord(request.getName(), request.getUserId(), request.getId());
        record.setId(UUID.randomUUID().toString());
        record.setName(record.getName());
        return record;
    }

    private DrinkRecord createRecordFromDrink(Drink drink) {
        DrinkRecord record = new DrinkRecord(drink.getId(), drink.getName(), drink.getUserId());
        record.setId(drink.getId());
        record.setName(drink.getName());
        record.setIngredients(drink.getIngredients());
        return record;


    }


    private Drink convertRecordToDrink(DrinkRecord record) {
        Drink drink = new Drink(record.getId(), record.getName(),record.getUserId());
        drink.setIngredients(record.getIngredients());
        return drink;
    }


}
