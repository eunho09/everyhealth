package com.example.everyhealth.controller;

import com.example.everyhealth.dto.LoginInfo;
import com.example.everyhealth.security.JwtTokenGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RestLoginController {

    private final JwtTokenGenerator jwtTokenGenerator;

    @GetMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/login");

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/login/check")
    public ResponseEntity<LoginInfo> loginCheck(HttpServletRequest request) {
        boolean check = false;
        LoginInfo loginInfo = new LoginInfo();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();

                    loginInfo.setId(jwtTokenGenerator.getUserId(token));
                    loginInfo.setName(jwtTokenGenerator.getName(token));
                    loginInfo.setPicture(jwtTokenGenerator.getPicture(token));

                    check = true;
                }
            }
        }

        loginInfo.setCheck(check);

        return ResponseEntity.ok().body(loginInfo);
    }
}