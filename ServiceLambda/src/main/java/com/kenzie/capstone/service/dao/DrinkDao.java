package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.List;

public interface DrinkDao {

    DrinkRecord addDrink(DrinkRecord drink);

    DrinkRecord getDrink(String id);

    DrinkRecord updateDrink(DrinkRecord drink);

    String deleteDrink(DrinkRecord drink);

    List<DrinkRecord> getAllDrinks();

    List<DrinkRecord> getDrinksByUserId(String userId);


}
