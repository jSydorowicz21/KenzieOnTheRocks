package com.kenzie.appserver.service.model;

import java.util.List;

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
}
