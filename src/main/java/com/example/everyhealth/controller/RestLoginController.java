package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RefreshToken;
import com.example.everyhealth.dto.LoginInfo;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RestLoginController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

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
                if ("access_token".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        /*Long memberId = request.get("memberId");

        // 리프레시 토큰 삭제
        refreshTokenService.deleteByMemberId(memberId);*/

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/login/check")
    public ResponseEntity<LoginInfo> loginCheck(HttpServletRequest request) {
        boolean check = false;
        LoginInfo loginInfo = new LoginInfo();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
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

    @PostMapping("/api/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        // 요청에서 리프레시 토큰을 가져옴
        String refreshToken = request.get("refreshToken");

        try {
            // 토큰 유효성 검증
            if (!jwtTokenGenerator.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            // DB에서 리프레시 토큰 조회
            Optional<RefreshToken> storedToken = refreshTokenService.findByToken(refreshToken);

            if (storedToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found in database");
            }

            RefreshToken tokenEntity = storedToken.get();

            // 토큰 만료 확인
            refreshTokenService.verifyExpiration(tokenEntity);

            // 사용자 ID로 회원 정보 조회
            Long memberId = tokenEntity.getMemberId();
            Member member = memberService.findById(memberId);

            // 새 액세스 토큰 생성
            String newAccessToken = jwtTokenGenerator.generateAccessToken(member);

            // 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", refreshToken); // 동일한 리프레시 토큰 유지

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }
}