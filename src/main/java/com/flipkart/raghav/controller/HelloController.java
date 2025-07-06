package com.flipkart.raghav.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {
    @GetMapping("/")
    public ResponseEntity<String> greet(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>("Welcome to Project:" + httpServletRequest.getSession().getId(), HttpStatus.OK);
    }

    @GetMapping("/about")
    public ResponseEntity<?> about() {
        return new ResponseEntity<>("About Page", HttpStatus.OK);
    }
}
