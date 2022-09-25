package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.DrinkRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DrinkRepository extends CrudRepository<DrinkRecord, String> {
    boolean UserHasExistingDrink(String name);
    DrinkRecord findDrinkByUserId(String userId);
}
