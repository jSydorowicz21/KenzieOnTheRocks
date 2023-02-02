package com.kenzie.appserver.service;

import com.kenzie.appserver.service.model.Drink;
import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.LambdaDrink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrinkService {
    private final LambdaServiceClient lambdaServiceClient;

    @Autowired
    public DrinkService(LambdaServiceClient lambdaServiceClient) {
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Drink findById(String id) {
        final LambdaDrink drink = lambdaServiceClient.getDrink(id);

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

    @ExcludeFromJacocoGeneratedReport
    public Drink getDrink(String name, String userId){
        if(name == null || userId == null){
            throw new IllegalArgumentException("Invalid drink name or user id");
        }

        final Drink drink = getAllDrinks().stream().filter(d -> d.getName().equals(name)
                && d.getUserId().equals(userId)).findFirst().orElse(null);

        if (drink == null){
            throw new IllegalArgumentException("Drink not found");
        }

        return drink;
    }

    //add drink
    public Drink addDrink(Drink request) {
        LambdaDrink drink = new LambdaDrink(request.getId(), request.getName(), request.getIngredients(), request.getUserId());
        lambdaServiceClient.addDrink(drink);

        return createDrinkFromLambda(drink);
    }

    public Drink updateDrink(Drink request) {
        final LambdaDrink updatedDrink = lambdaServiceClient.updateDrink(new LambdaDrink(request.getId(), request.getName(),
                request.getIngredients(), request.getUserId()));

        return createDrinkFromLambda(updatedDrink);
    }

    public List<Drink> getFilteredDrinks(List<String> ingredients){
        return lambdaServiceClient.getAllDrinks().stream()
                .filter(drink -> new HashSet<>(drink.getIngredients()).containsAll(ingredients))
                .map(drink -> new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId()))
                .collect(Collectors.toList());
    }

    public String delete(String id){
        final String response;
        try{
            response = lambdaServiceClient.deleteDrink(id);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is no matching drink");
        }
        return response;
    }

    public Drink createDrinkFromLambda(LambdaDrink drink){
        if(drink == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getUserId());
    }

}
