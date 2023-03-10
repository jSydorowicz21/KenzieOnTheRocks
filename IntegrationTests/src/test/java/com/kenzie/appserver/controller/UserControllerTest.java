package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.User;
import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void addUser() throws Exception {
        //GIVEN
        final UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUserId(UUID.randomUUID().toString());

        //WHEN
        final ResultActions actions = mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful());

        final String responseBody = actions.andReturn().getResponse().getContentAsString();
        final UserResponse response = mapper.readValue(responseBody, UserResponse.class);
        //THEN
        assertThat(response.getUserId()).isNotEmpty().as("The ID is populated");
    }

    @Test
    public void addNewDrink() throws Exception {
        //GIVEN
        final UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUserId(UUID.randomUUID().toString());

        final User userResponse = userService.addNewUser(userCreateRequest.getUserId());
        final Drink drink = new Drink("id", "somethng", List.of("new", "new"), userResponse.getUserId());
        final AddDrinkRequest addDrinkRequest = new AddDrinkRequest(userResponse.getUserId(), drink);

        userService.addDrinkToList(userResponse, addDrinkRequest.getDrink());

        //WHEN
        mvc.perform(post("/users/drinks")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addDrinkRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateUser() throws Exception{
        //GIVEN
        final UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUserId(UUID.randomUUID().toString());

        final User userResponse = userService.addNewUser(userCreateRequest.getUserId());

        final UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUserId(userResponse.getUserId());

        // WHEN
        mvc.perform(post("/users", userResponse.getUserId())
                        .content(mapper.writeValueAsString(updateRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    public void updateUserDrinks(){

    }

    @Test
    public void getUser() throws Exception{
        final UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUserId(UUID.randomUUID().toString());

        final User userResponse = userService.addNewUser(userCreateRequest.getUserId());

        mvc.perform(get("/users/{userId}", userResponse.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersDrinks() throws Exception{
        //GIVEN
        final UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUserId(UUID.randomUUID().toString());

        final User userResponse = userService.addNewUser(userCreateRequest.getUserId());

        final List<Drink> drinks = userService.addDrinkToList(userResponse, new Drink("test", "test", List.of("test"), userResponse.getUserId()));
        //WHEN
        final ResultActions actions = mvc.perform(get("/users/drinks/{userId}", userResponse.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());

        final String responseBody = actions.andReturn().getResponse().getContentAsString();
        final List<DrinkResponse> responses = mapper.readValue(responseBody, new TypeReference<>() {});

        assertThat(responses).isNotEmpty();
        assertThat(responses).containsAll(convertToResponses(drinks));
    }

    @ExcludeFromJacocoGeneratedReport
    private List<DrinkResponse> convertToResponses(List<Drink> drinks){
        final List<DrinkResponse> drinkResponses = new ArrayList<>();
        for (Drink drink : drinks){
            final DrinkResponse drinkResponse = new DrinkResponse();
            drinkResponse.setId(drink.getId());
            drinkResponse.setName(drink.getName());
            drinkResponse.setIngredients(drink.getIngredients());
            drinkResponse.setUserId(drink.getUserId());
            drinkResponses.add(drinkResponse);
        }
        return drinkResponses;
    }

}
