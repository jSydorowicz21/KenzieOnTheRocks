package com.kenzie.appserver.repositories.model;

import java.util.List;
import java.util.Objects;

//@DynamoDBTable(tableName = "Drinks")
public class DrinkRecord {
    private String id;
    private String name;
    private List<String> ingredients;
    private String userId;

    public DrinkRecord(String id, String name, List<String> ingredients, String userId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.userId = userId;
    }

    public DrinkRecord() {

    }

//    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

//    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @DynamoDBAttribute(attributeName = "ingredients")
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrinkRecord)) return false;
        DrinkRecord record = (DrinkRecord) o;
        return Objects.equals(getId(), record.getId()) && Objects.equals(getName(), record.getName()) && Objects.equals(getIngredients(), record.getIngredients()) && Objects.equals(getUserId(), record.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getIngredients(), getUserId());
    }
}
