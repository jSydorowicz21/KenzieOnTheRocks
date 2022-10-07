package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.DrinkRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class NonCachingDrinkDao implements DrinkDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public NonCachingDrinkDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public DrinkRecord addDrink(DrinkRecord drink) {


        try {
            mapper.save(drink, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return drink;
    }

    public DrinkRecord getDrink(String id) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);

        DynamoDBQueryExpression<DrinkRecord> queryExpression = new DynamoDBQueryExpression<DrinkRecord>()
                .withHashKeyValues(drinkRecord)
                .withConsistentRead(false);

        try {
            return mapper.query(DrinkRecord.class, queryExpression).get(0);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public DrinkRecord updateDrink(DrinkRecord drink) {

        try {
            mapper.save(drink, new DynamoDBSaveExpression());
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("update Failed");
        }

        return drink;
    }

    public List<DrinkRecord> getAllDrinks() {
        return mapper.scan(DrinkRecord.class, new DynamoDBScanExpression());
    }

    public List<DrinkRecord> getDrinksByUserId(String userId) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setUserId(userId);

        DynamoDBQueryExpression<DrinkRecord> queryExpression = new DynamoDBQueryExpression<DrinkRecord>()
                .withHashKeyValues(drinkRecord)
                .withConsistentRead(false);

        return mapper.query(DrinkRecord.class, queryExpression);
    }

    public void deleteDrink(DrinkRecord  drink) {

        mapper.delete(drink);

    }



}
