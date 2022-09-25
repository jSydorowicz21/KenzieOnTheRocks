package com.kenzie.appserver.service.model;

import java.util.List;
import java.util.Objects;

public class User {

    private String userId;

    private List<Drink> drinks;

    public User(){}

    public User(String userId){
        this.userId = userId;
    }

    public User(String userId, List<Drink> drinks){
        this.userId = userId;
        this.drinks = drinks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
