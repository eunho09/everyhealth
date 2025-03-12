package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;

    public JwtTokenGenerator(@Value("${jwt.secret}") String secretKey,
                             CustomUserDetailsService customUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Getter
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private long accessTokenExpiresIn;

        public TokenInfo(String accessToken, String refreshToken, long accessTokenExpiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.accessTokenExpiresIn = accessTokenExpiresIn;
        }
    }

    public TokenInfo generateTokenSet(Member member) {
        String accessToken = generateAccessToken(member);
        String refreshToken = generateRefreshToken(member.getId());

        long accessTokenExpiresIn = Instant.now()
                .plus(accessTokenExpireTime, ChronoUnit.MINUTES)
                .toEpochMilli();

        return new TokenInfo(accessToken, refreshToken, accessTokenExpiresIn);
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiryDate = Date.from(Instant.now().plus(accessTokenExpireTime, ChronoUnit.MINUTES));

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("authorities", member.getRole());
        claims.put("picture", member.getPicture());

        claims.put("tokenType", "access");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = Date.from(Instant.now().plus(refreshTokenExpireTime, ChronoUnit.DAYS));

        return Jwts.builder()
                .claim("id", userId)
                .claim("tokenType", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        Claims claims = parseClaims(refreshToken);
        String tokenType = claims.get("tokenType", String.class);

        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("유효하지 않은 토큰 타입입니다.");
        }

        Long userId = claims.get("id", Long.class);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());
        Member member = convertUserDetailsToMember(userDetails);

        return generateAccessToken(member);
    }

    private Member convertUserDetailsToMember(UserDetails userDetails) {
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