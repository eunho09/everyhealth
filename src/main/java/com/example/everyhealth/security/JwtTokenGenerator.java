package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class JwtTokenGenerator {
    private final CustomUserDetailsService customUserDetailsService;
    private final Key key;

    // 토큰 유효 시간 설정 (액세스 토큰: 30분, 리프레시 토큰: 7일)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30; // 분 단위
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7; // 일 단위

    public JwtTokenGenerator(@Value("${jwt.secret}") String secretKey,
                             CustomUserDetailsService customUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    // 토큰 응답 클래스
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private long accessTokenExpiresIn;

        public TokenInfo(String accessToken, String refreshToken, long accessTokenExpiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.accessTokenExpiresIn = accessTokenExpiresIn;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public long getAccessTokenExpiresIn() {
            return accessTokenExpiresIn;
        }
    }

    // 액세스 토큰과 리프레시 토큰을 함께 생성
    public TokenInfo generateTokenSet(Member member) {
        // 액세스 토큰 생성
        String accessToken = generateAccessToken(member);

        // 리프레시 토큰 생성
        String refreshToken = generateRefreshToken(member.getId());

        // 토큰 만료시간 계산 (밀리초 단위)
        long accessTokenExpiresIn = Instant.now()
                .plus(ACCESS_TOKEN_EXPIRE_TIME, ChronoUnit.MINUTES)
                .toEpochMilli();

        return new TokenInfo(accessToken, refreshToken, accessTokenExpiresIn);
    }

    // 액세스 토큰 생성 메소드
    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiryDate = Date.from(Instant.now().plus(ACCESS_TOKEN_EXPIRE_TIME, ChronoUnit.MINUTES));

        // 기존 generateToken 메소드와 동일한 클레임을 사용
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("authorities", member.getRole());
        claims.put("picture", member.getPicture());
        // 추가 회원 정보가 필요하다면 여기에 넣을 수 있음
        // claims.put("email", member.getEmail());
        // claims.put("nickname", member.getNickname());

        // 액세스 토큰임을 식별하기 위한 타입 추가
        claims.put("tokenType", "access");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 생성 메소드
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRE_TIME, ChronoUnit.DAYS));

        return Jwts.builder()
                .claim("id", userId)
                .claim("tokenType", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰으로 새 액세스 토큰 발급
    public String refreshAccessToken(String refreshToken) {
        // 리프레시 토큰 유효성 검증
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰에서 사용자 ID 추출
        Claims claims = parseClaims(refreshToken);
        String tokenType = claims.get("tokenType", String.class);

        // 토큰 타입 검증
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("유효하지 않은 토큰 타입입니다.");
        }

        Long userId = claims.get("id", Long.class);

        // 사용자 정보 조회 (loadUserByUsername 메서드를 통해 Member 객체로 변환 필요)
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

        // Member 객체로 변환 (실제 구현에서는 적절한 방법으로 Member 객체 가져오기)
        Member member = convertUserDetailsToMember(userDetails);

        // 새 액세스 토큰 생성
        return generateAccessToken(member);
    }

    // UserDetails를 Member로 변환하는 메소드 (실제 구현에 맞게 수정 필요)
    private Member convertUserDetailsToMember(UserDetails userDetails) {
        // 이 부분은 실제 애플리케이션의 구현에 맞게 작성해야 합니다.
        // 예시일 뿐, 실제로는 Member 클래스의 구조에 맞게 변환 로직을 작성해야 합니다.
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        } else{
            return null;
        }
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", Long.class);
    }

    public String getName(String token) {
        Claims claims = parseClaims(token);
        return claims.get("name", String.class);
    }

    public String getPicture(String token) {
        Claims claims = parseClaims(token);
        return claims.get("picture", String.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    // 인증 객체 생성
    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}