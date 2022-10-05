package com.kenzie.appserver;


import com.google.gson.Gson;
import com.kenzie.appserver.controller.model.DrinkCreateRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateJsonForTesting {

    public static void main(String[] args){
        DrinkCreateRequest request = new DrinkCreateRequest();
        request.setName("test");
        request.setId("testId");
        request.setIngredients(List.of("testIngredient", "testIngredient2"));
        request.setUserId("testUserId");


        Gson gson = new Gson();

        System.out.println(gson.toJson(request));

//         Json example to create a restaurant
//         {
//            "name":"HardlyFood",
//            "category":"good food",
//            "storeHours":[
//                "Monday: 9-5",
//                "Tuesday: 9-10"
//            ]
//        }

        // Json example to create a review
//         {
//            "restaurantId":"1",
//            "restaurantName":"Bobbies Bistro",
//            "userId":"73",
//            "title":"Bad Food",
//            "description":"food was alright, staff was bad",
//            "rating":"2",
//            "price":"10.50"
//        }
    }


}
