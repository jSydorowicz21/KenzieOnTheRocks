package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.kenzie.capstone.service.DrinkService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.LambdaDrink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        final Gson gson = new Gson();
        final String output;

        log.info(gson.toJson(input));

        final ServiceComponent serviceComponent = DaggerServiceComponent.create();
        final DrinkService lambdaService = serviceComponent.provideDrinkService();
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        final LambdaDrink lambdaDrink = gson.fromJson(input.getBody(), LambdaDrink.class);


        if (lambdaDrink == null || lambdaDrink.getId() == null) {
            return response
                    .withStatusCode(400)
                    .withBody("drink is invalid");
        }

        try {
            final LambdaDrink lambdaDrinkFromLambda = lambdaService.updateDrink(lambdaDrink);
            output = gson.toJson(lambdaDrinkFromLambda);
            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.getMessage()));
        }
    }
}
