package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {

        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(createUserResponse(user));
    }


    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest userCreateRequest){

        if (userCreateRequest == null || userCreateRequest.getUserId() == null){
            return ResponseEntity.badRequest().build();
        }

        User user = userService.addNewUser(userCreateRequest.getUserId());

        return ResponseEntity.ok(createUserResponse(user));

    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        if (userUpdateRequest == null || userUpdateRequest.getUserId() == null){
            return ResponseEntity.badRequest().build();
        }

        User user = userService.updateUser(new User(userUpdateRequest.getUserId(), userUpdateRequest.getDrinks()));

        return ResponseEntity.ok(createUserResponse(user));

    }

    @PutMapping("/drinks")
    public ResponseEntity<UserResponse> updateUserDrinks(@RequestBody UserUpdateRequest userUpdateRequest){
        if (userUpdateRequest == null || userUpdateRequest.getUserId() == null ||
                userUpdateRequest.getDrinks() == null || userUpdateRequest.getDrinks().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user = userService.updateUserDrinks(userService.getUserById(userUpdateRequest.getUserId()), userUpdateRequest.getDrinks());

        return ResponseEntity.ok(createUserResponse(user));

    }

    @GetMapping("/drinks/{userId}")
    public ResponseEntity<List<DrinkResponse>> getUsersDrinks(@PathVariable("userId") String userId){
        if (userId == null){
            return ResponseEntity.badRequest().build();
        }

        List<Drink> drinks = userService.getUsersDrinks(userService.getUserById(userId));

        List<DrinkResponse> drinkResponses = new ArrayList<>();
        for (Drink drink : drinks){
            drinkResponses.add(createDrinkResponse(drink));
        }

        return ResponseEntity.ok(drinkResponses);
    }

    private UserResponse createUserResponse(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setDrinks(user.getDrinks());

        return userResponse;
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
