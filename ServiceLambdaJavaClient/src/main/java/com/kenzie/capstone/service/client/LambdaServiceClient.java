package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.model.Drink;

import java.util.List;


public class LambdaServiceClient {

    private static final String DRINKS_ENDPOINT = "drinks";
    private static final String DRINKS_ID_ENDPOINT = "drinks/{id}";

    private static final String DRINKS_USER_ENDPOINT = "drinks/user/{id}";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    private Gson gson = new Gson();

    public Drink getDrink(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        Drink drink;
        try {
            drink = mapper.readValue(response, Drink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drink;
    }

    public Drink addDrink(Drink drink) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(DRINKS_ENDPOINT, gson.toJson(drink));
        try {
            drink = mapper.readValue(response, Drink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drink;
    }

    public Drink updateDrink(Drink drink) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.putEndpoint(DRINKS_ENDPOINT, gson.toJson(drink));
        try {
            drink = mapper.readValue(response, Drink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drink;
    }

    public Drink deleteDrink(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.deleteEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        try {
            return mapper.readValue(response, Drink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
    }

    public List<Drink> getAllDrinks() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_ENDPOINT);
        List<Drink> drinks;
        try {
            drinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, Drink.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drinks;
    }


    public List<Drink> getDrinksByUserId(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_USER_ENDPOINT.replace("{id}", id));
        List<Drink> drinks;
        try {
            drinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, Drink.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drinks;
    }
}
