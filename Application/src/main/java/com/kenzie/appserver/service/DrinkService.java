package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.model.Drink;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.stereotype.Service;

@Service
public class DrinkService {
    private DrinkRepository drinkRepository;
    private LambdaServiceClient lambdaServiceClient;

    public DrinkService(DrinkRepository drinkRepository, LambdaServiceClient lambdaServiceClient) {
        this.drinkRepository = drinkRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Drink findById(String id) {

        // Example getting data from the lambda
        ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);

        // Example getting data from the local repository
        Drink dataFromDynamo = drinkRepository
                .findById(id)
                .map(example -> new Drink(example.getId(), example.getName()))
                .orElse(null);

        return dataFromDynamo;
    }

    public Drink addNewExample(String name) {
        // Example sending data to the lambda
        ExampleData dataFromLambda = lambdaServiceClient.setExampleData(name);

        // Example sending data to the local repository
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(dataFromLambda.getId());
        drinkRecord.setName(dataFromLambda.getData());
        drinkRepository.save(drinkRecord);

        Drink drink = new Drink(dataFromLambda.getId(), name);
        return drink;
    }
}
