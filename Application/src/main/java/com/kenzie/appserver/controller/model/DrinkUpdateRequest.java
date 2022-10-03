package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DrinkUpdateRequest {
        @NotEmpty
        @JsonProperty("Id")
        public String Id;

        @NotEmpty
        @JsonProperty("name")
        private String name;

        @NotEmpty
        @JsonProperty("ingredients")
        private List<String> ingredients;

        @NotEmpty
        @JsonProperty("userId")
        private String userId;

        public String getUserId(){
            return userId;
        }

        public void setUserId(String userId){
            this.userId = userId;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }
}
