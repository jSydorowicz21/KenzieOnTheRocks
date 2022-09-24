package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.Drink;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "Drinks")
public class DrinkRecord {

    private String drinkID;

    private String userId;
    private String name;
    private List<String> ingredients;

    public DrinkRecord(){

    }

    public DrinkRecord(String drinkID, String name, String userId) {
        this.drinkID = drinkID;
        this.name = name;
        this.userId = userId;
    }



    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return drinkID;
    }

    @DynamoDBRangeKey(attributeName = "Name")
    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(String id) {
        this.drinkID = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Ingredients")
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DrinkRecord drinkRecord = (DrinkRecord) o;
        return Objects.equals(this.getId(), drinkRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
