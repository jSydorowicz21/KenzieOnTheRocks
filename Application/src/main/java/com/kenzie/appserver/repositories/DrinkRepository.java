package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.DrinkRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DrinkRepository extends CrudRepository<DrinkRecord, String> {
    boolean UserHasExistingDrink(String name);
    List<DrinkRecord> getUsersDrinks(String userId);
    DrinkRecord findDrinkByUserId(String userId);
}
