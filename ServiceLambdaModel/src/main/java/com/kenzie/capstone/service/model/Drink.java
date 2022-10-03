package com.kenzie.capstone.service.model;

import java.util.List;
import java.util.Objects;

class Drink{
    private String id;
    private String name;
    private List<String> ingredients;
    private String userId;

    public Drink(String id, String name, List<String> ingredients, String userId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.userId = userId;
    }

    public Drink() {}

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink)) return false;
        Drink drink = (Drink) o;
        return Objects.equals(id, drink.id) && Objects.equals(name, drink.name) && Objects.equals(ingredients, drink.ingredients) && Objects.equals(userId, drink.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients, userId);
    }
}
