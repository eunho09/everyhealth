package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    private final long ACCESS_TOKEN_MAX_AGE_SECONDS = 30 * 60; // 30분
    private final long REFRESH_TOKEN_MAX_AGE_SECONDS = 7 * 24 * 60 * 60; // 7일

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 사용자 정보 추출
        CustomOAuth2UserDetails customOAuth2UserDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        Member member = customOAuth2UserDetails.getMember();

        // 기존 방식의 JWT 토큰 생성 (필요한 경우)

        // 액세스 토큰과 리프레시 토큰 세트도 생성
        JwtTokenGenerator.TokenInfo tokenInfo = jwtTokenGenerator.generateTokenSet(member);

        // 리프레시 토큰 DB에 저장
        refreshTokenService.createRefreshToken(member.getId(), tokenInfo.getRefreshToken());

        // 액세스 토큰 쿠키 설정 (새로운 토큰 방식)
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", tokenInfo.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds(ACCESS_TOKEN_MAX_AGE_SECONDS))
                .sameSite("Lax")
                .build();

        // 리프레시 토큰 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenInfo.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/token/refresh") // 리프레시 토큰은 토큰 갱신 엔드포인트에서만 접근 가능하도록 제한
                .maxAge(Duration.ofSeconds(REFRESH_TOKEN_MAX_AGE_SECONDS))
                .sameSite("Lax")
                .build();

        // 쿠키 헤더 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()); // 새 액세스 토큰
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()); // 리프레시 토큰

        // 프론트엔드 리디렉션
        response.sendRedirect("http://localhost:3000");
    }
}