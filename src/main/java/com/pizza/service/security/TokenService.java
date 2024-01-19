package com.pizza.service.security;

import com.pizza.model.security.jwt.InvalidatedToken;
import com.pizza.model.security.responses.TokenRefreshResponse;
import com.pizza.repository.security.InvalidatedTokenRepository;
import com.pizza.repository.security.UserRepository;
import com.pizza.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${com.pizza.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${com.pizza.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<TokenRefreshResponse> processRefreshTokenRequest(String refreshToken, String deviceId) {
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

        invalidateTokens(refreshToken);

        String newAccessToken = jwtUtils.generateAccessJwtToken(username, deviceId);
        String newAccessTokenId = jwtUtils.getAccessTokenIdFromJwtToken(newAccessToken);
        String newRefreshToken = jwtUtils.generateRefreshJwtToken(username, deviceId, newAccessTokenId);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
    }

    public void invalidateTokens(String refreshToken) {
        invalidatedTokenRepository.save(invalidatedTokenFromRefreshToken(refreshToken));
        invalidatedTokenRepository.save(invalidatedTokenFromAccessToken(jwtUtils.getReferenceFromRefreshJwtToken(refreshToken), refreshToken));
    }

    private InvalidatedToken invalidatedTokenFromRefreshToken(String refreshToken) {
        return InvalidatedToken.builder()
                .tokenId(jwtUtils.getRefreshTokenIdFromJwtToken(refreshToken))
                .expiryDate(jwtUtils.getExpiryDateFromJwtToken(refreshToken).toInstant())
                .build();
    }

    private InvalidatedToken invalidatedTokenFromAccessToken(String accessTokenId, String refreshToken) {
        Instant expiryDate = (jwtUtils.getExpiryDateFromJwtToken(refreshToken).toInstant()).minusMillis(jwtRefreshExpirationMs).plusMillis(jwtExpirationMs);
        return InvalidatedToken.builder()
                .tokenId(accessTokenId)
                .expiryDate(expiryDate)
                .build();
    }

    @Scheduled(cron = "0 0 0 */2 * *") // aprox midnight, every 2 days
    public void cleanExpiredTokens() {
        invalidatedTokenRepository.deleteExpiredTokens();
    }

}
