package com.kenzie.appserver.service.model;

import java.util.List;
import java.util.Objects;

public class Drink {
    private final String id;
    private final String name;
    private List<String> ingredients;



    public Drink(String id, String name) {
        this.id = id;
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
