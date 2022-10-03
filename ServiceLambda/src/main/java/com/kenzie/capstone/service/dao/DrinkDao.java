package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.Drink;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.List;

public interface DrinkDao {

    DrinkRecord addDrink(DrinkRecord drink);

    DrinkRecord getDrink(String id);

    DrinkRecord updateDrink(DrinkRecord drink);

    void deleteDrink(DrinkRecord drink);

    List<DrinkRecord> getAllDrinks();

    List<DrinkRecord> getDrinksByUserId(String userId);


}
