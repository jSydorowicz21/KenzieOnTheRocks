package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.UserHasExistingDrinkException;
import com.kenzie.appserver.service.model.UserHasNoExistingDrinkException;
import com.kenzie.capstone.service.model.LambdaDrink;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DrinkService {
    private LambdaServiceClient lambdaServiceClient;

    @Autowired
    public DrinkService(LambdaServiceClient lambdaServiceClient) {
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Drink findById(String id) {

        LambdaDrink drink = lambdaServiceClient.getDrink(id);

        if (id == null || drink == null) {
            throw new IllegalArgumentException("Invalid drink id");
        }

        return createDrinkFromLambda(drink);
    }

    public List<Drink> getAllDrinks() {
        return lambdaServiceClient.getAllDrinks()
                .stream()
                .map(this::createDrinkFromLambda)
                .collect(Collectors.toList());
    }

    public Drink getDrink(String name, String userId){

        if(name == null || userId == null){
            throw new IllegalArgumentException("Invalid drink name or user id");
        }

        Drink drink = getAllDrinks().stream().filter(d -> d.getName().equals(name) && d.getUserId().equals(userId)).findFirst().orElse(null);

        if (drink == null){
            throw new IllegalArgumentException("Drink not found");
        }
        return drink;

    }

    //add drink
    public Drink addDrink(Drink request) {
        System.out.println(request.getId());
        LambdaDrink drinkExists = null;
        try {
            drinkExists = lambdaServiceClient.getDrink(request.getId());
        }
        catch (IllegalArgumentException e){{
            // do nothing
        }}


        if(drinkExists != null) {
            throw new UserHasExistingDrinkException("Drink should be updated instead");
        }
        LambdaDrink drink = new LambdaDrink(request.getId(), request.getName(), request.getIngredients(), request.getUserId());
        lambdaServiceClient.addDrink(drink);

        return createDrinkFromLambda(drink);
    }

    public Drink updateDrink(Drink request) {
        Drink drinkFromLambda;

        try {
            drinkFromLambda = findById(request.getId());
        }
        catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid drink id");
        }


        drinkFromLambda.setIngredients(request.getIngredients());

        LambdaDrink updatedDrink = lambdaServiceClient.updateDrink(new LambdaDrink(drinkFromLambda.getId(), drinkFromLambda.getName(),
                drinkFromLambda.getIngredients(), drinkFromLambda.getUserId()));

        return createDrinkFromLambda(updatedDrink);
    }

    public List<Drink> getFilteredDrinks(List<String> ingredients){
        return lambdaServiceClient.getAllDrinks().stream()
                .filter(drink -> new HashSet<>(drink.getIngredients()).containsAll(ingredients))
                .map(drink -> new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId()))
                .collect(Collectors.toList());
    }

    public void delete(String id){

        try{
            findById(id);
            lambdaServiceClient.deleteDrink(id);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is no matching drink");
        }
    }

    public Drink createDrinkFromLambda(LambdaDrink drink){
        if(drink == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId());
    }
    private DrinkRecord createRecordFromRequest(Drink request) {
        DrinkRecord record = new DrinkRecord(request.getName(), request.getUserId(), request.getIngredients(), request.getId());
        record.setId(UUID.randomUUID().toString());
        record.setName(record.getName());
        return record;
    }
}
