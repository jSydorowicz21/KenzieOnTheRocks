package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.DrinkData;


public class LambdaServiceClient {

    private static final String DRINKS_ENDPOINT = "drinks";
    private static final String DRINKS_ID_ENDPOINT = "drinks/{id}";

    private static final String DRINKS_USER_ENDPOINT = "drinks/user/{id}";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public DrinkData getExampleData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DRINKS_ID_ENDPOINT.replace("{id}", id));
        DrinkData drinkData;
        try {
            drinkData = mapper.readValue(response, DrinkData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drinkData;
    }

    public DrinkData setExampleData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(DRINKS_ENDPOINT, data);
        DrinkData drinkData;
        try {
            drinkData = mapper.readValue(response, DrinkData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return drinkData;
    }
}
