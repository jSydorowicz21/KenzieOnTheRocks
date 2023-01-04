package com.kenzie.appserver.controller;

import com.google.gson.Gson;
import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkDeleteRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;

import com.kenzie.appserver.service.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private DrinkService drinkService;
    private Gson gson = new Gson();


    @Autowired
    DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> get(@PathVariable("id") String id) {
        Drink drink;
        try {
             drink = drinkService.findById(id);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (drink == null) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(createDrinkResponse(drink));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<DrinkResponse>> getFilteredDrinks(@RequestBody List<String> ingredients) {

        List<Drink> filteredDrinks = drinkService.getFilteredDrinks(ingredients);

        if (filteredDrinks == null ||  filteredDrinks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<DrinkResponse> response = new ArrayList<>();
        for (Drink drink : filteredDrinks) {
            response.add(this.createDrinkResponse(drink));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DrinkResponse>> getAllDrinks() {
        List<Drink> drinks = drinkService.getAllDrinks();

        if (drinks == null ||  drinks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<DrinkResponse> response = new ArrayList<>();
        for (Drink drink : drinks) {
            response.add(this.createDrinkResponse(drink));
        }

        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<DrinkResponse> addNewDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) {

        if(drinkCreateRequest.getId() == null){
            drinkCreateRequest.setId(UUID.randomUUID().toString());
        }


        Drink drink = drinkService.addDrink(new Drink(drinkCreateRequest.getId(), drinkCreateRequest.getName(),
                drinkCreateRequest.getIngredients(), drinkCreateRequest.getUserId()));


        return ResponseEntity.created(URI.create("/drinks/" + drink.getId())).body(this.createDrinkResponse(drink));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DrinkResponse> deleteDrink(@PathVariable("id") String id) {
        try {
            drinkService.delete(id);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<DrinkResponse> updateDrink(@RequestBody DrinkUpdateRequest drinkUpdateRequest) {
        Drink drink = new Drink(
                drinkUpdateRequest.getId(),
                drinkUpdateRequest.getName(),
                drinkUpdateRequest.getIngredients(),
                drinkUpdateRequest.getUserId());
        drinkService.updateDrink(drink); // user DB

        DrinkResponse drinkResponse = createDrinkResponse(drink);

        return ResponseEntity.ok(drinkResponse);
    }

    private DrinkResponse createDrinkResponse(Drink drink) {
        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        drinkResponse.setUserId(drink.getUserId());
        drinkResponse.setIngredients(drink.getIngredients());
        return drinkResponse;
    }
}
