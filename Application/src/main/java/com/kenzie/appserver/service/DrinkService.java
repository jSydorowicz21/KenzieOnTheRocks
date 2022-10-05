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

        if (id == null || lambdaServiceClient.getDrink(id) == null) {
            throw new IllegalArgumentException("Invalid drink id");
        }

        return createDrinkFromLambda(lambdaServiceClient.getDrink(id));
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
        LambdaDrink drinkExists = lambdaServiceClient.getDrink(request.getId());

        if(drinkExists != null) {
            throw new UserHasExistingDrinkException("Drink should be updated instead");
        }
        LambdaDrink drink = new LambdaDrink(request.getId(), request.getName(), request.getIngredients(), request.getUserId());
        lambdaServiceClient.addDrink(drink);

        return createDrinkFromLambda(drink);
    }

    public Drink updateDrink(Drink request) {
        LambdaDrink drinkFromLambda = lambdaServiceClient.getDrink(request.getId());

        if(drinkFromLambda == null) {
            throw new UserHasNoExistingDrinkException("Drink should be added instead.");
        }

        drinkFromLambda.setIngredients(request.getIngredients());

        LambdaDrink updatedDrink = lambdaServiceClient.updateDrink(drinkFromLambda);

        return createDrinkFromLambda(updatedDrink);
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

    public Drink createDrinkFromLambda(LambdaDrink drink){
        return new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId());
    }
    private DrinkRecord createRecordFromRequest(Drink request) {
        DrinkRecord record = new DrinkRecord(request.getName(), request.getUserId(), request.getIngredients(), request.getId());
        record.setId(UUID.randomUUID().toString());
        record.setName(record.getName());
        return record;
    }
}
