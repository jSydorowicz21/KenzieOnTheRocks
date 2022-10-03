package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkDeleteRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;

import com.kenzie.appserver.service.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private DrinkService drinkService;

    @Autowired
    DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> get(@PathVariable("id") String id) {

        Drink drink = drinkService.findById(id);
        if (drink == null) {
            return ResponseEntity.notFound().build();
        }

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        return ResponseEntity.ok(drinkResponse);
    }


    @GetMapping
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
    public ResponseEntity<DrinkResponse> addNewDrink(@RequestBody Drink drinkCreateRequest) {
        com.kenzie.capstone.service.model.Drink drink = drinkService.addDrink(drinkCreateRequest);

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        drinkResponse.setUserId(drink.getUserId());
        drinkResponse.setIngredients(drink.getIngredients());

        return ResponseEntity.ok(drinkResponse);
    }

    @DeleteMapping
    public ResponseEntity<DrinkResponse> deleteDrink(@RequestBody DrinkDeleteRequest drinkDeleteRequest) {
        Drink drinkToDelete = new Drink(drinkDeleteRequest.getId(), drinkDeleteRequest.getUserId(), drinkDeleteRequest.getIngredients(), drinkDeleteRequest.getName());
        drinkService.delete(drinkToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<DrinkResponse> updateDrink(@RequestBody DrinkUpdateRequest drinkUpdateRequest) {
        Drink drink = new Drink(
                drinkUpdateRequest.getId(),
                drinkUpdateRequest.getName(),
                drinkUpdateRequest.getIngredients(),
                drinkUpdateRequest.getUserId());
        drink.setIngredients(drinkUpdateRequest.getIngredients());
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
