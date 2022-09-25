package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkDeleteRequest;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.model.Drink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void getById_Exists() throws Exception {
        String userId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String id = UUID.randomUUID().toString();

        Drink drink = new Drink(userId, name, id);
        Drink persistedDrink = drinkService.getDrink(drink.getName(), drink.getUserId());

        mvc.perform(get("/drinks/{id}", persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id")
                        .isString())
                .andExpect(jsonPath("name")
                        .value(is(name)))
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
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);

        // WHEN
        mvc.perform(post("/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(drinkCreateRequest)))
                // THEN
                .andExpect(jsonPath("userId")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateDrink_PutSuccessful() throws Exception {
        // GIVEN
        String userId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String id = UUID.randomUUID().toString();

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        drinkCreateRequest.setId(id);

        Drink persistedDrink = drinkService.addDrink(drinkCreateRequest);

        DrinkUpdateRequest drinkUpdateRequest = new DrinkUpdateRequest();
        drinkUpdateRequest.setUserId(userId);
        drinkUpdateRequest.setName("new Name");
        drinkUpdateRequest.setId(id);

        // WHEN
        mvc.perform(put("/drinks")
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
        String userId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String id = UUID.randomUUID().toString();

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest();
        drinkCreateRequest.setUserId(userId);
        drinkCreateRequest.setName(name);
        Drink persistedDrink = drinkService.addDrink(drinkCreateRequest);

        // WHEN
        mvc.perform(delete("/drinks", persistedDrink.getId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent())
                .assertThat(drinkService.findById(id)).isNull();
    }

}
