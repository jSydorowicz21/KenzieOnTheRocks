package com.kenzie.appserver.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import com.kenzie.appserver.service.model.Drink;

public class DrinkListConverter implements DynamoDBTypeConverter {
    Gson gson = new Gson();
    /**
     * Turns an object of type T into an object of type S.
     *
     * @param object
     */
    @Override
    public Object convert(Object object) {
        return gson.toJson(object);
    }

    /**
     * Turns an object of type S into an object of type T.
     *
     * @param object
     */
    @Override
    public Object unconvert(Object object) {
        return gson.fromJson((String) object, Drink.class);
    }
}
