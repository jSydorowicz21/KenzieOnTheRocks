package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.model.Drink;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DrinkService {
    private DrinkRepository drinkRepository;
    private LambdaServiceClient lambdaServiceClient;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository, LambdaServiceClient lambdaServiceClient) {
        this.drinkRepository = drinkRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Drink findById(String id) {

        Drink dataFromLambda = createDrinkFromLambda(lambdaServiceClient.getDrink(id));
        return dataFromLambda;
    }

    public List<Drink> getAllDrinks() {
        return lambdaServiceClient.getAllDrinks()
                .stream()
                .map(drink -> createDrinkFromLambda(drink))
                .collect(Collectors.toList());
    }

    public Drink getDrink(String name, String userId){
        List<Drink> drinks = getAllDrinks().stream().filter(drink -> drink.getUserId().equals(userId)).collect(Collectors.toList());
        return drinks.isEmpty() ? null : drinks.get(0);

    }

    //add drink
    public com.kenzie.capstone.service.model.Drink addDrink(Drink request) {
        com.kenzie.capstone.service.model.Drink drinkExists = lambdaServiceClient.getDrink(request.getId());

        if(drinkExists != null) {
            try {
                throw new Exception("has an existing drink. The existing drink should be updated instead");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DrinkRecord record = createRecordFromRequest(request);
        com.kenzie.capstone.service.model.Drink drink = new com.kenzie.capstone.service.model.Drink(record.getId(), record.getName(), record.getIngredients(), record.getUserId());
        lambdaServiceClient.addDrink(drink);

        return drink;
    }

    public Drink updateDrink(Drink request) {
        com.kenzie.capstone.service.model.Drink drinkDoesnotExists = lambdaServiceClient.getDrink(request.getId());

        if(drinkDoesnotExists == null) {
            try {
                throw new Exception("The drink does not exist, it should be added instead");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<com.kenzie.capstone.service.model.Drink> lambdaServiceClientDrinksByUserId = lambdaServiceClient.getDrinksByUserId(request.getUserId());

        com.kenzie.capstone.service.model.Drink updatedDrink = null;

        for(com.kenzie.capstone.service.model.Drink drink : lambdaServiceClientDrinksByUserId){
            if(drink.equals(request)){
                drink.setIngredients(request.getIngredients());
                updatedDrink = new com.kenzie.capstone.service.model.Drink(drink.getId(), drink.getName(), request.getIngredients(), drink.getUserId());
                lambdaServiceClient.updateDrink(updatedDrink);
            }
        }
        Drink serviceDrink = new Drink(updatedDrink.getId(), updatedDrink.getName(), updatedDrink.getIngredients(), updatedDrink.getUserId());
        return serviceDrink;
    }

    public List<Drink> getFilteredDrinks(List<String> ingredients){
        return lambdaServiceClient.getAllDrinks().stream()
                .filter(drink -> new HashSet<>(drink.getIngredients()).containsAll(ingredients))
                .map(drink -> new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId()))
                .collect(Collectors.toList());
    }

    public void delete(Drink drink){
        try{
            lambdaServiceClient.deleteDrink(drink.getId());
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is no matching drink");
        }
    }

    public Drink createDrinkFromLambda(com.kenzie.capstone.service.model.Drink drink){
        return new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId());
    }
    private DrinkRecord createRecordFromRequest(Drink request) {
        DrinkRecord record = new DrinkRecord(request.getName(), request.getUserId(), request.getIngredients(), request.getId());
        record.setId(UUID.randomUUID().toString());
        record.setName(record.getName());
        return record;
    }
}
