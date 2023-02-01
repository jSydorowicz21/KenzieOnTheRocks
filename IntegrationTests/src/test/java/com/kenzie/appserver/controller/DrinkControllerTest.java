package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.model.Drink;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class DrinkControllerTest {
    @Autowired
    private MockMvc mvc;

    private final Gson gson = new Gson();

    @Autowired
    DrinkService drinkService;

    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<String> ingredients = List.of("Rum","Coke","Ice");
    private final List<String> idsToBeDeleted = new ArrayList<>();

    @AfterEach
    public void tearDown() {
        if (!idsToBeDeleted.isEmpty()) {
            for(String id : idsToBeDeleted) {
                drinkService.delete(id);
            }
        }
        idsToBeDeleted.clear();
    }

    @Test
    public void getFilteredDrinks() throws Exception{
        //GIVEN
        final String id = UUID.randomUUID().toString();
        final String name = mockNeat.strings().valStr();
        final String userId = UUID.randomUUID().toString();

        final  Drink drink = new Drink(id, name, ingredients, userId);
        final Drink drink2 = new Drink(UUID.randomUUID().toString(), "name2", List.of("nothing", "More nothing"), userId);
        final Drink persistedDrink = drinkService.addDrink(drink);
        drinkService.addDrink(drink2);

        idsToBeDeleted.add(id);
        idsToBeDeleted.add(drink2.getId());

        //WHEN
        final ResultActions actions = mvc.perform(get("/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(gson.toJson(ingredients)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        final String responseBody = actions.andReturn().getResponse().getContentAsString();
        final List<DrinkResponse> responses = mapper.readValue(responseBody, new TypeReference<>() {});

        //THEN
        assertThat(responses.size() == 1).as("There are responses");
        for (DrinkResponse response : responses) {
            assertThat(response.getId().equals(persistedDrink.getId())).as("Id matches");
            assertThat(response.getName().equals(persistedDrink.getName())).as("The name matches");
            assertThat(response.getIngredients().equals(persistedDrink.getIngredients())).as("Ingredients match");
            assertThat(response.getUserId().equals(persistedDrink.getUserId())).as("User id matches");
        }
    }
    @Test
    public void getById_Exists() throws Exception {
        //GIVEN
        final String id = UUID.randomUUID().toString();
        final String name = mockNeat.strings().valStr();
        final String userId = UUID.randomUUID().toString();

        final Drink drink = new Drink(id, name, ingredients, userId);
        final Drink persistedDrink = drinkService.addDrink(drink);

        idsToBeDeleted.add(id);

        //WHEN
        mvc.perform(get("/drinks/{id}", persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(jsonPath("id")
                        .isString())
                .andExpect(jsonPath("name")
                        .value(name))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void filteredSearch_returns_matching_drink() throws Exception {
        //GIVEN
        final String id = UUID.randomUUID().toString();
        final String userId = UUID.randomUUID().toString();

        final Drink drink2 = new Drink("new Id", "new Name", List.of("Whiskey", "Coke", "Ice"), userId);

        idsToBeDeleted.add(id);
        idsToBeDeleted.add(drink2.getId());

        //WHEN
        final ResultActions actions = mvc.perform(get("/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ingredients))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        final String responseBody = actions.andReturn().getResponse().getContentAsString();
        final List<DrinkResponse> responses = mapper.readValue(responseBody, new TypeReference<>() {
        });

        //THEN
        assertThat(responses.size()).isGreaterThan(0).as("There are responses");
        for (DrinkResponse response : responses) {
            assertThat(response.getId()).isNotEmpty().as("The ID is populated");
            assertThat(response.getName()).isNotEmpty().as("The name is populated");
            assertThat(response.getIngredients()).isNotEmpty().as("Ingredients are populated");
            assertThat(response.getIngredients()).isNotEmpty().as("Ingredients are populated");
        }
    }
    @Test
    public void getDrink_DrinkDoesNotExist() throws Exception {
        //GIVEN
        final String id = UUID.randomUUID().toString();
        //WHEN
        mvc.perform(get("/drinks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDrink_CreateSuccessful() throws Exception {
        //GIVEN
        final String userId = UUID.randomUUID().toString();
        final String name = mockNeat.strings().valStr();
        final String id = UUID.randomUUID().toString();

        final DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setIngredients(ingredients);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);

        idsToBeDeleted.add(id);

        //WHEN
        mvc.perform(post("/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(drinkCreateRequest)))
                //THEN
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateDrink_PutSuccessful() throws Exception {
        //GIVEN
        final String id = UUID.randomUUID().toString();
        final String name = mockNeat.strings().valStr();
        final String userId = UUID.randomUUID().toString();

        final DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);
        drinkCreateRequest.setIngredients(ingredients);
        final Drink drink = new Drink(id, name, ingredients, userId);
        final Drink persistedDrink = drinkService.addDrink(drink);

        final DrinkUpdateRequest drinkUpdateRequest = new DrinkUpdateRequest();

        drinkUpdateRequest.setUserId(persistedDrink.getUserId());
        drinkUpdateRequest.setName("new Name");
        drinkUpdateRequest.setId(persistedDrink.getId());
        drinkUpdateRequest.setIngredients(List.of("Mojito", "long island"));

        idsToBeDeleted.add(id);

        //WHEN
        mvc.perform(put("/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(drinkUpdateRequest)))
                //THEN
                .andExpect(jsonPath("userId")
                        .exists())
                .andExpect(jsonPath("name")
                        .value("new Name"))
                .andExpect(jsonPath("id")
                        .value(id))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDrink_DeleteSuccessful() throws Exception {
        //GIVEN
        final String id = UUID.randomUUID().toString();
        final String name = mockNeat.strings().valStr();
        final String userId = UUID.randomUUID().toString();

        final DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setIngredients(ingredients);
        final Drink drink = new Drink(id, name, ingredients, userId);
        final Drink persistedDrink = drinkService.addDrink(drink);

        //WHEN
        mvc.perform(delete("/drinks/" + persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());
    }

}
