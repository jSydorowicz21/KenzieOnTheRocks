package com.kenzie.appserver.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.List;
import java.util.Objects;
@DynamoDBDocument
public class Drink {
    private String id;
    private String name;
    private List<String> ingredients;
    private String userId;

    public Drink(){}

    public Drink(String id, String name, List<String> ingredients, String userId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients(){
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink)) return false;
        Drink drink = (Drink) o;
        return Objects.equals(getId(), drink.getId()) && Objects.equals(getName(), drink.getName()) && Objects.equals(getIngredients(), drink.getIngredients()) && Objects.equals(getUserId(), drink.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getIngredients(), getUserId());
    }

    @Override
    public String toString() {
        return "Drink{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", userId='" + userId + '\'' +
                '}';
    }
}
