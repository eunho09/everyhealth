package com.example.everyhealth.service;

import com.example.everyhealth.domain.RefreshToken;
import com.example.everyhealth.repository.RefreshTokenRepository;
import com.example.everyhealth.security.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expire-time}")
    private Long refreshTokenExpirationDays;

    @Transactional
    public RefreshToken createRefreshToken(Long memberId, String token) {
        Instant expiryDate = Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS);

        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByMemberId(memberId);

        if (refreshTokenOpt.isPresent()) {
            RefreshToken existingToken = refreshTokenOpt.get();
            existingToken.updateToken(token, expiryDate);
            return refreshTokenRepository.save(existingToken);
        } else {
            RefreshToken refreshToken = new RefreshToken(memberId, token, expiryDate);

            return refreshTokenRepository.save(refreshToken);
        }
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId);
    }

    @Transactional
    public void verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
    }

    @Transactional
    public void deleteByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    @Transactional
    public void update(RefreshToken refreshToken, String token) {
        Instant expiryDate = Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS);
        refreshToken.updateToken(token, expiryDate);
    }
}