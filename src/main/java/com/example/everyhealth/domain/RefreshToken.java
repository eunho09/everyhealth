package com.example.everyhealth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;

@Entity
@Getter
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    private Long memberId;
    private String token;
    private Instant expiryDate;

    public RefreshToken(Long memberId, String token, Instant expiryDate) {
        this.memberId = memberId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    protected RefreshToken() {
    }

    public void updateToken(String token, Instant expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }
}
