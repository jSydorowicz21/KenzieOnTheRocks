package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.UserHasExistingDrinkException;
import com.kenzie.appserver.service.model.UserHasNoExistingDrinkException;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.kenzie.capstone.service.model.LambdaDrink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class DrinkServiceTest {
    private DrinkRepository drinkRepository;
    private DrinkService drinkService;
    private LambdaServiceClient lambdaServiceClient;

    //constant testing values
    public static final String TEST_USERID = "test userid";
    public static final String TEST_USERID_ALT = "other test userid";
    public static final String TEST_DRINK_ID ="test drinkId";
    public static final String TEST_DRINK_ID_ALT = "other test drink id";
    public static final String TEST_DRINK_NAME =" test drink name";
    public static final String TEST_DRINK_NAME_ALT = "other test drink name";
    public static final List<String> TEST_DRINK_INGREDIENTS = List.of("Ingredient1", "Ingredient2");
    public static final List<String> TEST_DRINK_INGREDIENTS_ALT = List.of("Ingredient3", "Ingredient4");

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

        LambdaDrink drink = new LambdaDrink();
        drink.setId(id);
        drink.setName(TEST_DRINK_NAME);
        drink.setUserId(TEST_USERID);

        // WHEN
        when(lambdaServiceClient.getDrink(id)).thenReturn(drink);
        Drink returnedDrink = drinkService.findById(id);

        // THEN
        Assertions.assertNotNull(returnedDrink, "The object is returned");
        Assertions.assertEquals(returnedDrink.getId(), drink.getId(), "The id matches");
        Assertions.assertEquals(returnedDrink.getName(), drink.getName(), "The name matches");
    }
//
//    @Test
//    void findByConcertId_invalid() {
//        // GIVEN
//        String id = randomUUID().toString();
//
//        when(drinkRepository.findById(id)).thenReturn(Optional.empty());
//
//        // WHEN
//        Drink drink = drinkService.findById(id);
//
//        // THEN
//        Assertions.assertNull(drink, "The example is null when not found");
//    }
    /** ------------------------------------------------------------------------
     *  drinkService.getDrinks
     *  ------------------------------------------------------------------------ **/

    //Todo Fix to throw exception
    @Test
    void getDrink_noMatchingDrink_Throws_Exception() {

        //when

        when(lambdaServiceClient.getDrink(any())).thenReturn(null);

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> drinkService.getDrink(TEST_DRINK_ID, TEST_USERID));

    }
    /** ------------------------------------------------------------------------
     *  drinkService.getAllDrinks
     *  ------------------------------------------------------------------------ **/

    @Test
    void getAllDrinks_sameUser_returnsAll(){
        //Given
        LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        LambdaDrink drink2 = new LambdaDrink(TEST_DRINK_ID_ALT, TEST_DRINK_NAME_ALT, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);



        when(lambdaServiceClient.getAllDrinks()).thenReturn(List.of(drink,drink2));

            //when
        List<Drink> drinks = drinkService.getAllDrinks();
        //then
        Assertions.assertNotNull(drinks,"A list should be returned");
        Assertions.assertEquals(2, drinks.size(), "All the drinks should be in list");


    }
    @Test
    void getAllDrinks_noDrinkMatch_returnsEmptyList(){
        //Given


        when(lambdaServiceClient.getAllDrinks()).thenReturn(Collections.emptyList());

        //when
        List<Drink> drinks = drinkService.getAllDrinks();
        //then
        Assertions.assertNotNull(drinks, "An empty list will be returned");
        Assertions.assertTrue(drinks.isEmpty(),"The list should be empty as no drink found ");

    }
    /** ------------------------------------------------------------------------
     *  drinkService.deleteDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void deleteDrink_drinkExists_verifyInteractions() {
        //given
        Drink drink = new Drink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        when(lambdaServiceClient.deleteDrink(TEST_DRINK_ID)).thenReturn(mock(LambdaDrink.class));

        //when
        drinkService.delete(drink);

        //then
        verify(lambdaServiceClient).deleteDrink(TEST_DRINK_ID);

    }
    /** ------------------------------------------------------------------------
     *  drinkService.addDrink
     *  ------------------------------------------------------------------------ **/
    @Test
    void addNewDrink_userHasNotExistingDrink_addDrink() {
        //given
        Drink request = new Drink(TEST_DRINK_ID,TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);


        LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);

        when(lambdaServiceClient.addDrink(drink)).thenReturn(drink);
        ArgumentCaptor<LambdaDrink> recordArgumentCaptor = ArgumentCaptor.forClass(LambdaDrink.class);

        //when
        Drink returnedDrink = drinkService.addDrink(request);
        //then
        Assertions.assertNotNull(returnedDrink);
        verify(lambdaServiceClient).addDrink(recordArgumentCaptor.capture());

        LambdaDrink record = recordArgumentCaptor.getValue();

        Assertions.assertNotNull(returnedDrink,"The new drink should be returned");
        Assertions.assertEquals(TEST_USERID,returnedDrink.getUserId(),"the userId should be the test user id");
        Assertions.assertEquals(TEST_DRINK_ID,returnedDrink.getId(), "the drink id should be the test drink id");
        Assertions.assertEquals(TEST_DRINK_NAME,returnedDrink.getName(),"the drink name should be the test drink name");
        Assertions.assertEquals(TEST_USERID,record.getUserId(),"the userId should be the test user id");
        Assertions.assertEquals(TEST_DRINK_ID, record.getId(),"the drink id should be the test drink id");
        Assertions.assertEquals(TEST_DRINK_NAME,record.getName(),"the drink name should be the test drink name");

    }
    @Test
    void addNewDrink_userHasExistingDrink_throwsException() {
        //given
        when(lambdaServiceClient.getDrink(any())).thenReturn(mock(LambdaDrink.class));

        //when then
        Assertions.assertThrows(UserHasExistingDrinkException.class,() -> drinkService.addDrink(mock(Drink.class)));

    }

    /** ------------------------------------------------------------------------
     *  drinkService.getUpdatedDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateDrink_dataValidAndMatches_returnsUpdatedDrink(){
        //Given
        LambdaDrink existingDrink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        LambdaDrink updatedDrink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);

        Drink request = new Drink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);


        when(lambdaServiceClient.getDrink(TEST_DRINK_ID)).thenReturn(existingDrink);
        when(lambdaServiceClient.updateDrink(any())).thenReturn(updatedDrink);

        ArgumentCaptor<LambdaDrink> recordArgumentCaptor = ArgumentCaptor.forClass(LambdaDrink.class);

        //when
        Drink drink = drinkService.updateDrink(request);

        //then
        verify(lambdaServiceClient).updateDrink(recordArgumentCaptor.capture());
        LambdaDrink recordWithUpdatedValues = recordArgumentCaptor.getValue();

        Assertions.assertNotNull(drink, "the newly updated drink should be returned");
        Assertions.assertEquals(TEST_DRINK_ID, recordWithUpdatedValues.getId(),"The drink id should match");
        Assertions.assertEquals(TEST_DRINK_NAME,recordWithUpdatedValues.getName()," test drink name should match");
        Assertions.assertEquals(TEST_USERID,recordWithUpdatedValues.getUserId(),"userid should match");
        Assertions.assertEquals(TEST_DRINK_ID, drink.getId(),"The drink id should match");
        Assertions.assertEquals(TEST_DRINK_NAME,drink.getName()," test drink name should match");
        Assertions.assertEquals(TEST_USERID,drink.getUserId(),"userid should match");

    }

    @Test
    void updateDrink_userHasNoExistingDrink_throwsException(){
        when(lambdaServiceClient.getDrink(any())).thenReturn(null);

        //when then
        Assertions.assertThrows(UserHasNoExistingDrinkException.class,()-> drinkService.updateDrink(mock(Drink.class)));

    }

    private DrinkRecord createRecord(String id, String name, String userId){
        DrinkRecord record = new DrinkRecord();
        record.setId(id);
        record.setName(name);
        record.setUserId(userId);
        return record;

    }


}
