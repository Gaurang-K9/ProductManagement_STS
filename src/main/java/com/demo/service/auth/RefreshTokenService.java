package com.demo.service.auth;

import com.demo.exception.BadRequestException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.auth.RefreshToken;
import com.demo.model.user.User;
import com.demo.repo.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setCreatedAt(Instant.now());
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepo.findByUser(user);
    }

    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RefreshToken.class, "userId", userId));
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.delete(token);
            throw new BadRequestException("Refresh Token expired. Please login again.");
        }

        if (token.getCreatedAt().plus(30, ChronoUnit.DAYS).isBefore(Instant.now())) {
            refreshTokenRepo.delete(token);
            throw new BadRequestException("Session expired. Please login again.");
        }
    }

    public void deleteByUser(User user) {
        refreshTokenRepo.deleteByUser(user);
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenRepo.save(refreshToken);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepo.delete(refreshToken);
    }
}
