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
import java.util.List;
import java.util.Map;

public class GetDrinksByUserId implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        final Gson gson = new Gson();
        final String output;
        final String userId;

        log.info(gson.toJson(input));

        final ServiceComponent serviceComponent = DaggerServiceComponent.create();
        final DrinkService lambdaService = serviceComponent.provideDrinkService();
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        userId = input.getBody();

        if (userId == null) {
            return response
                    .withStatusCode(400)
                    .withBody("UserId is invalid");
        }

        try {
            final List<LambdaDrink> lambdaDrinkFromLambda = lambdaService.getDrinksByUserId(userId);
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
