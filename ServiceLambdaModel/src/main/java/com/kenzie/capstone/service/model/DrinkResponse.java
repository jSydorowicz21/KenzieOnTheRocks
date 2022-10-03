package com.kenzie.capstone.service.model;

public class DrinkResponse {

    private String id;
    private String name;

    public DrinkResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public DrinkResponse(){}

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

    @Override
    public String toString() {
        return "DrinkResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
