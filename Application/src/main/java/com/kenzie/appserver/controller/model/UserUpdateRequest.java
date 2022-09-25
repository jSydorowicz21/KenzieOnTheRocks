package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Drink;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserUpdateRequest {
    @NotEmpty
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("drinks")
    private List<Drink> drinks;


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
}
