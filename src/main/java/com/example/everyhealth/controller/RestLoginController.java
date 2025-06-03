package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.aop.TokenAspect;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RefreshToken;
import com.example.everyhealth.dto.LoginInfo;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "로그인 관리")
public class RestLoginController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.access-token-expire-time}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire-time}")
    private Long refreshTokenExpireTime;

    @PostMapping("/api/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @ExtractMemberId Long memberId) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("cookieName={}, cookieValue={}", cookie.getName(), cookie.getValue());
                if ("access_token".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else if ("refresh_token".equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    cookie.setPath("/api/token/refresh");
                    response.addCookie(cookie);
                }
            }
        }

        refreshTokenService.deleteByMemberId(memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/login/check")
    @Operation(summary = "로그인 상태 체크")
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
        loginInfo.setExpiresIn(accessTokenExpireTime);

        return ResponseEntity.ok().body(loginInfo);
    }

    @PostMapping("/api/token/refresh")
    @Operation(summary = "리프레쉬 토큰으로 로그인")
    public ResponseEntity<String> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response) {
        if (!jwtTokenGenerator.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken);

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found in database");
        }

        refreshTokenService.verifyExpiration(token.get());

        String accessToken = jwtTokenGenerator.refreshAccessToken(token.get().getToken());
        String newRefreshToken = jwtTokenGenerator.generateRefreshToken(token.get().getMemberId());
        refreshTokenService.update(token.get(), newRefreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(accessTokenExpireTime))
                .sameSite("LAX")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/token/refresh")
                .maxAge(Duration.ofDays(refreshTokenExpireTime))
                .sameSite("LAX")
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok().body("토큰이 갱신되었습니다.");
    }
}