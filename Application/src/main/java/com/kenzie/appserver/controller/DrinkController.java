package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.service.DrinkService;

import com.kenzie.appserver.service.model.Drink;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/example")
public class DrinkController {

    private DrinkService drinkService;

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

    @PostMapping
    public ResponseEntity<DrinkResponse> addNewExample(@RequestBody DrinkCreateRequest drinkCreateRequest) {
        Drink drink = drinkService.addNewExample(drinkCreateRequest.getName());

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());

        return ResponseEntity.ok(drinkResponse);
    }
}
