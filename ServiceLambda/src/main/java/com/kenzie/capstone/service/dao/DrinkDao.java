package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.DrinkRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class DrinkDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public DrinkDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public ExampleData storeExampleData(ExampleData exampleData) {
        try {
            mapper.save(exampleData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return exampleData;
    }

    public List<DrinkRecord> getExampleData(String id) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);

        DynamoDBQueryExpression<DrinkRecord> queryExpression = new DynamoDBQueryExpression<DrinkRecord>()
                .withHashKeyValues(drinkRecord)
                .withConsistentRead(false);

        return mapper.query(DrinkRecord.class, queryExpression);
    }

    public DrinkRecord setExampleData(String id, String data) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);
        drinkRecord.setData(data);

        try {
            mapper.save(drinkRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return drinkRecord;
    }
}
