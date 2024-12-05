package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsMutator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class JwtTokenGenerator {

    private final Key key;

    public JwtTokenGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(Member member) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("authorities", member.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
                )
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateToken(OAuth2User oAuth2User) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("sub", oAuth2User.getName());
        claims.put("email", oAuth2User.getAttribute("email"));
        claims.put("authorities",
                oAuth2User.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
                )
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
