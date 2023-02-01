package com.kenzie.appserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @GetMapping("/")
    public ResponseEntity getServiceName() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
