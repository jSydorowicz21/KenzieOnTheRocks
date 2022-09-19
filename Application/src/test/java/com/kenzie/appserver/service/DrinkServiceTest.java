package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DrinkServiceTest {
    private DrinkRepository drinkRepository;
    private DrinkService drinkService;
    private LambdaServiceClient lambdaServiceClient;

    @BeforeEach
    void setup() {
        drinkRepository = mock(DrinkRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        drinkService = new DrinkService(drinkRepository, lambdaServiceClient);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String id = randomUUID().toString();

        DrinkRecord record = new DrinkRecord(id, "drinkName");


        // WHEN
        when(drinkRepository.findById(id)).thenReturn(Optional.of(record));
        Drink drink = drinkService.findById(id);

        // THEN
        Assertions.assertNotNull(drink, "The object is returned");
        Assertions.assertEquals(record.getId(), drink.getId(), "The id matches");
        Assertions.assertEquals(record.getName(), drink.getName(), "The name matches");
    }

    @Test
    void findByConcertId_invalid() {
        // GIVEN
        String id = randomUUID().toString();

        when(drinkRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Drink drink = drinkService.findById(id);

        // THEN
        Assertions.assertNull(drink, "The example is null when not found");
    }

}
