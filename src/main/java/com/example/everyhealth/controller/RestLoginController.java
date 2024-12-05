package com.example.everyhealth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
public class RestLoginController {

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToFrontendLoginPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/login");

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}