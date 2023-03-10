package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/drinks")
public class DrinkController {
    private final DrinkService drinkService;
    private final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Autowired
    DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> get(@PathVariable("id") String id) {
        final Drink drink;

        try {
             drink = drinkService.findById(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        if (drink == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(createDrinkResponse(drink));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<DrinkResponse>> getFilteredDrinks(@RequestBody List<String> ingredients) {
        final List<Drink> filteredDrinks = drinkService.getFilteredDrinks(ingredients);

        if (filteredDrinks == null ||  filteredDrinks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        final List<DrinkResponse> response = new ArrayList<>();

        for (Drink drink : filteredDrinks) {
            response.add(this.createDrinkResponse(drink));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DrinkResponse>> getAllDrinks() {
        final List<Drink> drinks = drinkService.getAllDrinks();

        if (drinks == null ||  drinks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        final List<DrinkResponse> response = new ArrayList<>();

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

        final Drink drink = drinkService.addDrink(new Drink(drinkCreateRequest.getId(), drinkCreateRequest.getName(),
                drinkCreateRequest.getIngredients(), drinkCreateRequest.getUserId()));

        return ResponseEntity.created(URI.create("/drinks/" + drink.getId())).body(this.createDrinkResponse(drink));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DrinkResponse> deleteDrink(@PathVariable("id") String id) {
        if (!UUID_REGEX.matcher(id).matches()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            drinkService.delete(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<DrinkResponse> updateDrink(@RequestBody DrinkUpdateRequest drinkUpdateRequest) {
        final Drink drink = new Drink(
                drinkUpdateRequest.getId(),
                drinkUpdateRequest.getName(),
                drinkUpdateRequest.getIngredients(),
                drinkUpdateRequest.getUserId());
        drinkService.updateDrink(drink); // user DB

        final DrinkResponse drinkResponse = createDrinkResponse(drink);

        return ResponseEntity.ok(drinkResponse);
    }

    private DrinkResponse createDrinkResponse(Drink drink) {
        final DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        drinkResponse.setUserId(drink.getUserId());
        drinkResponse.setIngredients(drink.getIngredients());
        return drinkResponse;
    }
}
