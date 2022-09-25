package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class DrinkDeleteRequest {
        @NotEmpty
        @JsonProperty("userId")
        private String userId;

        @NotEmpty
        @JsonProperty("name")
        private String name;

        @NotEmpty
        @JsonProperty("Id")
        private String Id;

        public String getId() {
            return Id;
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
