package com.kenzie.capstone.service.model;

import java.util.List;

public class DrinkRequest {

    private String id;
    private String name;
    private List<String> ingredients;
    private String userId;

    public DrinkRequest(String id, String name, List<String> ingredients, String userId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.userId = userId;
    }

    public DrinkRequest() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DrinkRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
