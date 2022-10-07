package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.model.Drink;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@IntegrationTest
class DrinkControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    DrinkService drinkService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private final List<String> ingredients = List.of("Rum","Coke","Ice");

    List<String> idsToBeDeleted = new ArrayList<>();

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
    public void getById_Exists() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String userId = UUID.randomUUID().toString();


        Drink drink = new Drink(id, name, ingredients, userId);
        Drink persistedDrink = drinkService.addDrink(drink);

        idsToBeDeleted.add(id);

        mvc.perform(get("/drinks/{id}", persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id")
                        .isString())
                .andExpect(jsonPath("name")
                        .value(name))
                .andExpect(status().is2xxSuccessful());
        }
    @Test
    public void getDrink_DrinkDoesNotExist() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        // WHEN
        mvc.perform(get("/drinks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDrink_CreateSuccessful() throws Exception {
        // GIVEN
        String userId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String id = UUID.randomUUID().toString();

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setIngredients(ingredients);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);

        idsToBeDeleted.add(id);

        // WHEN
        mvc.perform(post("/drinks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(drinkCreateRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateDrink_PutSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String userId = UUID.randomUUID().toString();

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);
        drinkCreateRequest.setIngredients(ingredients);
        Drink drink = new Drink(id, name, ingredients, userId);
        Drink persistedDrink = drinkService.addDrink(drink);

        DrinkUpdateRequest drinkUpdateRequest = new DrinkUpdateRequest();

        drinkUpdateRequest.setUserId(persistedDrink.getUserId());
        drinkUpdateRequest.setName("new Name");
        drinkUpdateRequest.setId(persistedDrink.getId());
        drinkUpdateRequest.setIngredients(List.of("Mojito", "long island"));

        idsToBeDeleted.add(id);


//         WHEN
        mvc.perform(put("/drinks/{id}", persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(drinkCreateRequest)))
                // THEN
                .andExpect(jsonPath("userId")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is("new Name")))
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDrink_DeleteSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String userId = UUID.randomUUID().toString();

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setIngredients(ingredients);
        Drink drink = new Drink(id, name, ingredients, userId);
        Drink persistedDrink = drinkService.addDrink(drink);

        // WHEN
        mvc.perform(delete("/drinks/" + persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk());
    }

}
