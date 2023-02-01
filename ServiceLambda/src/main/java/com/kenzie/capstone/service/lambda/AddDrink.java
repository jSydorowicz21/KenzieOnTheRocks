package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.DrinkService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.DrinkRequest;
import com.kenzie.capstone.service.model.LambdaDrink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        DrinkRequest drinkRequest;

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        DrinkService drinkService = serviceComponent.provideDrinkService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            drinkRequest = this.convert(input.getBody());
        } catch (InvalidDataException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }
        LambdaDrink lambdaDrink = drinkService.addDrink(drinkRequest);
        return response
                .withStatusCode(200)
                .withBody(gson.toJson(lambdaDrink));
    }

    public DrinkRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(body, DrinkRequest.class);
        } catch (Exception e) {
            throw new InvalidDataException("Referral could not be deserialized");
        }
    }
}
