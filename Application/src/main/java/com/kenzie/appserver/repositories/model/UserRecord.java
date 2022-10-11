package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.appserver.repositories.DrinkListConverter;
import com.kenzie.appserver.service.model.Drink;

import java.util.List;

@DynamoDBTable(tableName = "Users")
public class UserRecord {

    private String userId;

    private List<Drink> drinks;




    @DynamoDBHashKey(attributeName = "UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "Drinks")
    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}