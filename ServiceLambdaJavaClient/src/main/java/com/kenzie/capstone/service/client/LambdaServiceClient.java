package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.model.LambdaDrink;

import java.util.List;


public class LambdaServiceClient {

    private static final String DRINKS_ENDPOINT = "drinks";
    private static final String DRINKS_ID_ENDPOINT = "drinks/{id}";
    //possible endpoint for search filter??
    private static final String DRINKS_INGREDIENTS_ENDPOINT = "drinks/{ingredients}";

    private static final String DRINKS_USER_ENDPOINT = "drinks/user/{id}";

    private final ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    private final Gson gson = new Gson();

    public LambdaDrink getDrink(String id) {

        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        LambdaDrink lambdaDrink;

        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return lambdaDrink;
    }

    public LambdaDrink addDrink(LambdaDrink lambdaDrink) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(DRINKS_ENDPOINT, gson.toJson(lambdaDrink));
        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrink;
    }

    public LambdaDrink updateDrink(LambdaDrink lambdaDrink) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.putEndpoint(DRINKS_ENDPOINT, gson.toJson(lambdaDrink));
        try {
            lambdaDrink = mapper.readValue(response, LambdaDrink.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrink;
    }

    public void deleteDrink(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();

        endpointUtility.deleteEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
    }

    public List<LambdaDrink> getAllDrinks() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_ENDPOINT);
        List<LambdaDrink> lambdaDrinks;
        try {
            lambdaDrinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, LambdaDrink.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrinks;
    }


    public List<LambdaDrink> getDrinksByUserId(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_USER_ENDPOINT.replace("{id}", id));
        List<LambdaDrink> lambdaDrinks;
        try {
            lambdaDrinks = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, LambdaDrink.class));
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return lambdaDrinks;
    }
}
