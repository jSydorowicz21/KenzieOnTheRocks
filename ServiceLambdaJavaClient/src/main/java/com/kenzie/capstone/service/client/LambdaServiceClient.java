package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.model.LambdaDrink;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaServiceClient {
    private static final String DRINKS_ENDPOINT = "drinks";
    private static final String DRINKS_ID_ENDPOINT = "drinks/{id}";

    private static final String DRINKS_USER_ENDPOINT = "drinks/user/{id}";

    private final ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    private final Gson gson = new Gson();

    public LambdaDrink getDrink(String id) {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.getEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        final LambdaDrink lambdaDrink;

        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return lambdaDrink;
    }

    public LambdaDrink addDrink(LambdaDrink lambdaDrink) {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.postEndpoint(DRINKS_ENDPOINT, gson.toJson(lambdaDrink));
        if (response == null){
            throw new ApiGatewayException("Drink already exists!");
        }
        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrink;
    }

    public LambdaDrink updateDrink(LambdaDrink lambdaDrink) {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.putEndpoint(DRINKS_ENDPOINT, gson.toJson(lambdaDrink));
        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrink;
    }

    public String deleteDrink(String id) {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.deleteEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        if (response == null){
            throw new ApiGatewayException("Failed to delete drink with ID: " + id);
        }
        return response;
    }

    public List<LambdaDrink> getAllDrinks() {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.getEndpoint(DRINKS_ENDPOINT);
        final List<String> lambdaDrinks;
        try {
            lambdaDrinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            return lambdaDrinks.stream().map(drink -> gson.fromJson(drink, LambdaDrink.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
    }


    public List<LambdaDrink> getDrinksByUserId(String id) {
        final EndpointUtility endpointUtility = new EndpointUtility();
        final String response = endpointUtility.getEndpoint(DRINKS_USER_ENDPOINT.replace("{id}", id));
        final List<LambdaDrink> lambdaDrinks;
        try {
            lambdaDrinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, LambdaDrink.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrinks;
    }
}
