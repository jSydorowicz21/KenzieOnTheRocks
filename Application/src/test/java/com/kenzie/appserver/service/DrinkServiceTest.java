package com.kenzie.appserver.service;

import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.UserHasExistingDrinkException;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.LambdaDrink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class DrinkServiceTest {
    private DrinkService drinkService;
    private LambdaServiceClient lambdaServiceClient;

    //constant testing values
    public static final String TEST_USERID = "test userid";
    public static final String TEST_DRINK_ID ="test drinkId";
    public static final String TEST_DRINK_ID_ALT = "other test drink id";
    public static final String TEST_DRINK_NAME =" test drink name";
    public static final String TEST_DRINK_NAME_ALT = "other test drink name";
    public static final List<String> TEST_DRINK_INGREDIENTS = List.of("Ingredient1", "Ingredient2");
    public static final List<String> TEST_DRINK_INGREDIENTS_ALT = List.of("Ingredient3", "Ingredient4");

    @BeforeEach
    void setup() {
        lambdaServiceClient = mock(LambdaServiceClient.class);
        drinkService = new DrinkService(lambdaServiceClient);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void getDrinkById() {
        // GIVEN
        final String id = randomUUID().toString();

        LambdaDrink drink = new LambdaDrink();
        drink.setId(id);
        drink.setName(TEST_DRINK_NAME);
        drink.setUserId(TEST_USERID);

        // WHEN
        when(lambdaServiceClient.getDrink(id)).thenReturn(drink);
        final Drink returnedDrink = drinkService.findById(id);

        // THEN
        Assertions.assertNotNull(returnedDrink, "The object is returned");
        Assertions.assertEquals(returnedDrink.getId(), drink.getId(), "The id matches");
        Assertions.assertEquals(returnedDrink.getName(), drink.getName(), "The name matches");
    }
    /** ------------------------------------------------------------------------
     *  drinkService.getDrinks
     *  ------------------------------------------------------------------------ **/
    @Test
    void getDrink_noMatchingDrink_Throws_Exception() {
        //WHEN
        when(lambdaServiceClient.getDrink(any())).thenReturn(null);

        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> drinkService.getDrink(TEST_DRINK_ID, TEST_USERID));
    }
    /** ------------------------------------------------------------------------
     *  drinkService.getAllDrinks
     *  ------------------------------------------------------------------------ **/

    @Test
    void getAllDrinks_sameUser_returnsAll(){
        //GIVEN
        LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        LambdaDrink drink2 = new LambdaDrink(TEST_DRINK_ID_ALT, TEST_DRINK_NAME_ALT, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);

            //WHEN
        when(lambdaServiceClient.getAllDrinks()).thenReturn(List.of(drink,drink2));
        final List<Drink> drinks = drinkService.getAllDrinks();

        //THEN
        Assertions.assertNotNull(drinks,"A list should be returned");
        Assertions.assertEquals(2, drinks.size(), "All the drinks should be in list");


    }
    @Test
    void getAllDrinks_noDrinkMatch_returnsEmptyList(){
        //GIVEN

        //WHEN
        when(lambdaServiceClient.getAllDrinks()).thenReturn(Collections.emptyList());
        final List<Drink> drinks = drinkService.getAllDrinks();

        //THEN
        Assertions.assertNotNull(drinks, "An empty list will be returned");
        assertTrue(drinks.isEmpty(),"The list should be empty as no drink found ");

    }
    /** ------------------------------------------------------------------------
     *  drinkService.deleteDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void deleteDrink_drinkExists_verifyInteractions() {
        //GIVEN
        when(lambdaServiceClient.getDrink(TEST_DRINK_ID)).thenReturn(mock(LambdaDrink.class));

        //WHEN
        drinkService.delete(TEST_DRINK_ID);

        //THEN
        verify(lambdaServiceClient).deleteDrink(TEST_DRINK_ID);
    }
    /** ------------------------------------------------------------------------
     *  drinkService.addDrink
     *  ------------------------------------------------------------------------ **/
    @Test
    void addNewDrink_userHasNotExistingDrink_addDrink() {
        //GIVEN
        final Drink request = new Drink(TEST_DRINK_ID,TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        final LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);

        //WHEN
        when(lambdaServiceClient.addDrink(drink)).thenReturn(drink);
        final ArgumentCaptor<LambdaDrink> recordArgumentCaptor = ArgumentCaptor.forClass(LambdaDrink.class);
        final Drink returnedDrink = drinkService.addDrink(request);

        //THEN
        Assertions.assertNotNull(returnedDrink);
        verify(lambdaServiceClient).addDrink(recordArgumentCaptor.capture());

        final LambdaDrink record = recordArgumentCaptor.getValue();

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
        //GIVEN
        when(lambdaServiceClient.getDrink(any())).thenReturn(mock(LambdaDrink.class));

        //WHEN
        when(lambdaServiceClient.getDrink(any())).thenReturn(mock(LambdaDrink.class));
        //THEN
        Assertions.assertThrows(UserHasExistingDrinkException.class,() -> drinkService.addDrink(mock(Drink.class)));
    }

    /** ------------------------------------------------------------------------
     *  drinkService.getUpdatedDrink
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateDrink_dataValidAndMatches_returnsUpdatedDrink(){
        //GIVEN
        final LambdaDrink existingDrink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS, TEST_USERID);
        final LambdaDrink updatedDrink = new LambdaDrink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);

        final Drink request = new Drink(TEST_DRINK_ID, TEST_DRINK_NAME, TEST_DRINK_INGREDIENTS_ALT, TEST_USERID);

        //WHEN
        when(lambdaServiceClient.getDrink(TEST_DRINK_ID)).thenReturn(existingDrink);
        when(lambdaServiceClient.updateDrink(any())).thenReturn(updatedDrink);

        final ArgumentCaptor<LambdaDrink> recordArgumentCaptor = ArgumentCaptor.forClass(LambdaDrink.class);
        final Drink drink = drinkService.updateDrink(request);

        //THEN
        verify(lambdaServiceClient).updateDrink(recordArgumentCaptor.capture());
        final LambdaDrink recordWithUpdatedValues = recordArgumentCaptor.getValue();

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
        //GIVEN

        //WHEN
        when(lambdaServiceClient.getDrink(any())).thenReturn(mock(LambdaDrink.class));

        //THEN
        Assertions.assertThrows(ResponseStatusException.class,()-> drinkService.updateDrink(mock(Drink.class)));
    }

    @Test
    void filterDrink_matching_ingredient_returns_drink(){
        //GIVEN
        final LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID,TEST_DRINK_NAME,TEST_DRINK_INGREDIENTS,TEST_USERID);
        final LambdaDrink secondDrink = new LambdaDrink("random ID", "drink name", List.of("Ingredient1","Ingredient5"),TEST_USERID);
        final LambdaDrink altDrink = new LambdaDrink(TEST_DRINK_ID_ALT, TEST_DRINK_NAME_ALT, TEST_DRINK_INGREDIENTS_ALT,TEST_USERID);

        lambdaServiceClient.addDrink(drink);
        final Drink drink1 = new Drink(drink.getId(),drink.getName(),drink.getIngredients(),drink.getUserId());
        lambdaServiceClient.addDrink(altDrink);
        final Drink drink2 = new Drink(altDrink.getId(), altDrink.getName(), altDrink.getIngredients(),altDrink.getUserId());
        lambdaServiceClient.addDrink(secondDrink);
        final Drink drink3 = new Drink(secondDrink.getId(),secondDrink.getName(),secondDrink.getIngredients(),secondDrink.getUserId());

        //WHEN
        when(lambdaServiceClient.getAllDrinks()).thenReturn(List.of(drink,altDrink,secondDrink));
        final List<Drink> filteredList = drinkService.getFilteredDrinks(List.of("Ingredient1"));

        //THEN
        Assertions.assertTrue(filteredList.contains(drink1));
        Assertions.assertTrue(filteredList.contains(drink3));
        Assertions.assertFalse(filteredList.contains(drink2));
    }

    @Test
    void filterDrink_no_match_empty_list(){
        //GIVEN
        final LambdaDrink drink = new LambdaDrink(TEST_DRINK_ID,TEST_DRINK_NAME,TEST_DRINK_INGREDIENTS,TEST_USERID);
        final LambdaDrink secondDrink = new LambdaDrink("random ID", "drink name", List.of("Ingredient1","Ingredient5"),TEST_USERID);
        final LambdaDrink altDrink = new LambdaDrink(TEST_DRINK_ID_ALT, TEST_DRINK_NAME_ALT, TEST_DRINK_INGREDIENTS_ALT,TEST_USERID);

        lambdaServiceClient.addDrink(drink);
        final Drink drink1 = new Drink(drink.getId(),drink.getName(),drink.getIngredients(),drink.getUserId());
        lambdaServiceClient.addDrink(altDrink);
        final Drink drink2 = new Drink(altDrink.getId(), altDrink.getName(), altDrink.getIngredients(),altDrink.getUserId());
        lambdaServiceClient.addDrink(secondDrink);
        final Drink drink3 = new Drink(secondDrink.getId(),secondDrink.getName(),secondDrink.getIngredients(),secondDrink.getUserId());

        when(lambdaServiceClient.getAllDrinks()).thenReturn(List.of(drink,altDrink,secondDrink));
        //WHEN

        final List<Drink> filteredList = drinkService.getFilteredDrinks(List.of("Ingredient7"));
        //THEN
        Assertions.assertFalse(filteredList.contains(drink1));
        Assertions.assertFalse(filteredList.contains(drink2));
        Assertions.assertFalse(filteredList.contains(drink3));
        Assertions.assertTrue(filteredList.isEmpty());
    }

    @Test
    public void getDrink_null_drink_throwsException(){
        //GIVEN

        //WHEN
        when(lambdaServiceClient.getDrink(TEST_DRINK_ID)).thenReturn(mock(LambdaDrink.class));
        drinkService.delete(TEST_DRINK_ID);

        //THEN
        Assertions.assertThrows(IllegalArgumentException.class,() -> drinkService.getDrink(TEST_DRINK_NAME,TEST_USERID),"drink has been deleted");
    }

    @Test
    public void getDrink_null_name_throwsException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> drinkService.getDrink(null,"id"),"name cannot be null");
    }

    @Test
    public void getDrink_null_id_throwsException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> drinkService.getDrink("name",null),"id cannot be null");
    }
}
