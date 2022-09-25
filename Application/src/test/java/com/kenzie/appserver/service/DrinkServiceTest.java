package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
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
    public static final String Test_USERID_ALT = "other test userid";
    public static final String TEST_DRINK_ID ="test drinkid";
    public static final String TEST_DRINK_ID_ALT = "other test drink id";
    public static final String TEST_DRINK_NAME =" test drink name";
    public static final String TEST_DRINK_NAME_ALT = "other test drink name";

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

        DrinkRecord record = new DrinkRecord();
        record.setId(id);
        record.setName("concertname");

        // WHEN
        when(drinkRepository.findById(id)).thenReturn(Optional.of(record));
        Drink drink = drinkService.findById(id);

        // THEN
        Assertions.assertNotNull(drink, "The object is returned");
        Assertions.assertEquals(record.getId(), drink.getId(), "The id matches");
        Assertions.assertEquals(record.getName(), drink.getName(), "The name matches");
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

    @Test
    void getDrink_noMatchingDrink_returnsNull() {
        DrinkRecord record1 = createRecord(TEST_DRINK_ID,TEST_USERID,TEST_DRINK_NAME);
        when(drinkRepository.findAll()).thenReturn(Collections.singleton(record1));

        //when
        Drink drink = drinkService.getDrink(TEST_DRINK_ID_ALT,TEST_USERID);

        //then
        Assertions.assertNull(drink,"if no drink available with the id then it should return null");

    }
    /** ------------------------------------------------------------------------
     *  drinkService.getAllDrinks
     *  ------------------------------------------------------------------------ **/

    @Test
    void getAllDrinks_sameUser_returnsAll(){
        //Given
        DrinkRecord record1 = createRecord(TEST_DRINK_ID,TEST_DRINK_NAME,TEST_USERID);
        DrinkRecord record2 = createRecord(TEST_DRINK_ID_ALT,TEST_DRINK_NAME_ALT,TEST_USERID);

            List<DrinkRecord> recordList = new ArrayList<>();
            recordList.add(record1);
            recordList.add(record2);
            when(drinkRepository.findAll()).thenReturn(recordList);

            //when
        List<Drink> drinks = drinkService.getAllDrinks();
        //then
        Assertions.assertNotNull(drinks,"A list should be returned");
        Assertions.assertEquals(2, drinks.size(), "All the drinks should be in list");


    }
    @Test
    void getAllDrinks_noDrinkMatch_returnsEmptyList(){
        //Given
        DrinkRecord record1 = createRecord(TEST_DRINK_ID,TEST_DRINK_NAME,TEST_USERID);
        DrinkRecord record2 = createRecord(TEST_DRINK_ID_ALT,TEST_DRINK_NAME_ALT,TEST_USERID);

        List<DrinkRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(drinkRepository.findAll()).thenReturn(recordList);

        //when
        List<Drink> drinks = drinkService.getAllDrinks();
        //then
        Assertions.assertNotNull(drinks, "An empty list will be returned");
        Assertions.assertTrue(drinks.isEmpty(),"The list should be empty as no drink found with the drink name");

    }
    /** ------------------------------------------------------------------------
     *  drinkService.deleteDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void deleteDrink_drinkExists_verifyInteractions() {
        //given
        doNothing().when(drinkRepository).delete(mock(DrinkRecord.class));

        //when
        drinkService.delete(mock(Drink.class));

        //then
        verify(drinkRepository).delete(any());

    }
    /** ------------------------------------------------------------------------
     *  drinkService.addDrink
     *  ------------------------------------------------------------------------ **/
    @Test
    void addNewDrink_userHasNotExistingDrink_addDrink() {
        //given
        DrinkCreateRequest request = new DrinkCreateRequest();
        request.setId(TEST_DRINK_ID);
        request.setName(TEST_DRINK_NAME);
        request.setUserId(TEST_USERID);

        when(drinkService.findById(any())).thenReturn(mock(Drink.class));
        ArgumentCaptor<DrinkRecord> recordArgumentCaptor = ArgumentCaptor.forClass(DrinkRecord.class);

        //when
        Drink returnedDrink = drinkService.addDrink(request);
        //then
        Assertions.assertNotNull(returnedDrink);
        verify(drinkRepository).save(recordArgumentCaptor.capture());

        DrinkRecord record = recordArgumentCaptor.getValue();

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
        when(drinkService.findById(any())).thenReturn(mock(Drink.class));
        when(drinkRepository.UserHasExistingDrink(any())).thenReturn(true);

        //when then
        Assertions.assertThrows(UserHasExistingDrinkException.class,() ->drinkService.addDrink(mock(DrinkCreateRequest.class)));
        verify(drinkService).findById(any());
        verify(drinkRepository).UserHasExistingDrink(any());
        verifyNoMoreInteractions(drinkService);

    }

    /** ------------------------------------------------------------------------
     *  drinkService.getUpdatedDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateDrink_dataValidAndMatches_returnsUpdatedDrink(){
        //Given
        DrinkRecord existingRecord = createRecord(TEST_DRINK_ID,TEST_DRINK_NAME,TEST_USERID);

        DrinkUpdateRequest request = new DrinkUpdateRequest();
        request.setId(TEST_DRINK_ID);
        request.setName(TEST_DRINK_NAME);
        request.setUserId(TEST_USERID);

        when(drinkService.findById(TEST_USERID)).thenReturn(mock(Drink.class));
        when(drinkRepository.UserHasExistingDrink(any())).thenReturn(true);
        when(drinkRepository.findDrinkByUserId(any())).thenReturn(existingRecord);

        ArgumentCaptor<DrinkRecord> recordArgumentCaptor = ArgumentCaptor.forClass(DrinkRecord.class);

        //when
        Drink drink = drinkService.updateDrink(request);

        //then
        verify(drinkRepository).save(recordArgumentCaptor.capture());
        DrinkRecord recordWithUpdatedValues = recordArgumentCaptor.getValue();

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
        when(drinkService.findById(any())).thenReturn(mock(Drink.class));
        when(drinkRepository.UserHasExistingDrink(any())).thenReturn(false);

        //when then
        Assertions.assertThrows(UserHasNoExistingDrinkException.class,()-> drinkService.updateDrink(mock(DrinkUpdateRequest.class)));
        verify(drinkService).findById(any());
        verify(drinkRepository).UserHasExistingDrink(any());
        verifyNoMoreInteractions(drinkService);

    }

    private DrinkRecord createRecord(String id, String name, String userId){
        DrinkRecord record = new DrinkRecord();
        record.setId(id);
        record.setName(name);
        record.setUserId(userId);
        return record;

    }


}
