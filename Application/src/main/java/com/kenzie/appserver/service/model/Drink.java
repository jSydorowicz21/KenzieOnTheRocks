package com.kenzie.appserver.service.model;

import java.util.List;
import java.util.Objects;

public class Drink {
    private String id;
    private String name;
    private List<String> ingredients;
    private String userId;


    public Drink(){}

    public Drink(String id, String name, List<String> ingredients){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }
    public Drink(String id, String name) {
        this.id = id;
        this.name = name;
    }
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
        return Objects.equals(getId(), drink.getId()) && Objects.equals(getName(), drink.getName()) && Objects.equals(getIngredients(), drink.getIngredients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getIngredients());
    }
    @Override
    public String toString() {
        return "Drink{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

}
