package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.CachingDrinkDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.DrinkRequest;
import com.kenzie.capstone.service.model.LambdaDrink;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {


    private CachingDrinkDao cachingDrinkDao;
    private DrinkService drinkService;

    @BeforeAll
    void setup() {
        this.cachingDrinkDao = mock(CachingDrinkDao.class);
        this.drinkService = new DrinkService(cachingDrinkDao);
    }

    @Test
    void addDrinkTest() {
        ArgumentCaptor<DrinkRecord> drinkCaptor = ArgumentCaptor.forClass(DrinkRecord.class);


        // GIVEN
        String id = "drink_Id";
        String name = "drink_name";

        DrinkRequest request = new DrinkRequest();
        request.setId(id);
        request.setName(name);


        // WHEN
        LambdaDrink response = this.drinkService.addDrink(request);

        // THEN
        verify(cachingDrinkDao, times(1)).addDrink(drinkCaptor.capture());
        DrinkRecord record = drinkCaptor.getValue();

        assertNotNull(record, "The record is valid");
        assertEquals(id, record.getId(), "The drink id should match");
        assertEquals(name,record.getName(),"The drink name should match");


        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name,response.getName(),"The drink name should match");


    }


//    @Test
//    void addDrinkTest_no_drink_id() {
//
//        // GIVEN
//        String id = "";
//        String name ="";
//
//        DrinkRequest request = new DrinkRequest();
//        request.setId(id);
//        request.setName(name);
//
//        //when then
//        assertThrows(InvalidDataException.class, ()->this.drinkService.addDrink(request));
//
//    }

    @Test
    void updateDrinkTest() {
        ArgumentCaptor<DrinkRecord> drinkCaptor = ArgumentCaptor.forClass(DrinkRecord.class);
        // GIVEN
        String id = "drink_Id";
        String name = "drink_name";
        List<String>ingredients = new ArrayList<>();


        LambdaDrink request = new LambdaDrink();
        request.setId(id);
        request.setName(name);
        request.setIngredients(ingredients);


        // WHEN
        LambdaDrink response = this.drinkService.updateDrink(request);

        // THEN
        verify(cachingDrinkDao, times(1)).updateDrink(drinkCaptor.capture());
        DrinkRecord record = drinkCaptor.getValue();

        assertNotNull(record, "The record is valid");
        assertEquals(id, record.getId(), "The drink id should match");
        assertEquals(name,record.getName(),"The drink name should match");
        assertEquals(ingredients,record.getIngredients(),"Ingredients should match");


        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name,response.getName(),"The drink name should match");
        assertEquals(ingredients,response.getIngredients(),"ingredients should match");


    }

    @Test
    void deleteDrinkTest() {
        ArgumentCaptor<DrinkRecord> drinkCaptor = ArgumentCaptor.forClass(DrinkRecord.class);
        // GIVEN
        String id = "drink_Id";

        LambdaDrink request = new LambdaDrink();
        request.setId(id);

        //when
        drinkService.deleteDrink(id);
        //then
        verify(cachingDrinkDao, times(1)).deleteDrink(drinkCaptor.capture());

    }
    @Test
    void getAllDrinksTest() {
        //Given


        List<DrinkRecord> record = new ArrayList<>();

        DrinkRecord record1 = new DrinkRecord();
        record1.setId("drink1");
        record1.setName("drink_n1");
        record.add(record1);

        DrinkRecord record2 = new DrinkRecord();
        record2.setId("drink2");
        record2.setName("drink_n2");
        record.add(record2);

        when(cachingDrinkDao.getAllDrinks()).thenReturn(record);

        //when
        List<LambdaDrink> drinks = this.drinkService.getAllDrinks();

        //then
        verify(cachingDrinkDao,times(1)).getAllDrinks();
        assertEquals(2,drinks.size(),"drink has 2 items");

    }

    @Test
    void getDrinkByUserId() {

        //Given
        String userId = "userId";
        List<DrinkRecord> recordList = new ArrayList<>();

        DrinkRecord record1 = new DrinkRecord();
        record1.setId("drink1");
        record1.setName("drink_n1");
        record1.setUserId(userId);

        DrinkRecord record2 = new DrinkRecord();
        record2.setId("drink2");
        record2.setName("drink_n2");
        record2.setUserId(userId);

        when(cachingDrinkDao.getDrinksByUserId(userId)).thenReturn(recordList);

        //when
        List<LambdaDrink> drinks = this.drinkService.getDrinksByUserId(userId);

        //then
        verify(cachingDrinkDao,times(1)).getDrinksByUserId(userId);
        assertNotNull(drinks," drinks list will appear");

    }
//    @Test
//    void getDrinkTest() {
//
//        // GIVEN
//        String id = "drink_Id";
//        String name = "drink_name";
//
//        DrinkRequest request = new DrinkRequest();
//        request.setId(id);
//        request.setName(name);
//
//
//        // WHEN
//        LambdaDrink response = this.drinkService.getDrink(id);
//
//        // THEN
//        verify(cachingDrinkDao, times(1)).getDrink(id);
//
//        assertEquals(id, response.getId(), "The response id should match");
//
//    }

}

