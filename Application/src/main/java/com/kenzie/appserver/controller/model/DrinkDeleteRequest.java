package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DrinkDeleteRequest {
        @NotEmpty
        @JsonProperty("userId")
        private String userId;

        @NotEmpty
        @JsonProperty("name")
        private String name;

        @NotEmpty
        @JsonProperty("ingredients")
        private List<String> ingredients;

        @NotEmpty
        @JsonProperty("Id")
        private String Id;

        public String getId() {
            return Id;
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
        this.name = name;
    }
    }
