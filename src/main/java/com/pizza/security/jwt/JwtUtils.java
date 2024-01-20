package com.pizza.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    protected static final String ACCESS_TOKEN_ID = "access_token_id";
    protected static final String REFRESH_TOKEN_ID = "refresh_token_id";
    protected static final String DEVICE_ID = "device_id";
    protected static final String REFERENCE = "reference";

    @Value("${com.pizza.app.jwtSecret}")
    private String jwtSecret;

    @Value("${com.pizza.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${com.pizza.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    //todo remove this is only for device id encoding
    @Autowired
    private PasswordEncoder passwordEncoder;

    public SecretKey generateKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return new SecretKeySpec(bytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateAccessJwtToken(String username, String deviceId) {
        String accessTokenId = UUID.randomUUID().toString();
        String encodedDeviceId= encodeDeviceId(deviceId);

        Claims customClaims = Jwts.claims();
        customClaims.put(ACCESS_TOKEN_ID, accessTokenId);
        customClaims.put(DEVICE_ID, encodedDeviceId);

        return Jwts.builder()
                .setClaims(customClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(this.generateKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshJwtToken(String username, String deviceId, String accessTokenId) {
        String refreshTokenId = UUID.randomUUID().toString();
        String encodedDeviceId= encodeDeviceId(deviceId);

        Claims customClaims = Jwts.claims();
        customClaims.put(REFRESH_TOKEN_ID, refreshTokenId);
        customClaims.put(DEVICE_ID, encodedDeviceId);
        customClaims.put(REFERENCE, accessTokenId);

        return Jwts.builder()
                .setClaims(customClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .setClaims(customClaims)
                .signWith(this.generateKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpiryDateFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public String getAccessTokenIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().get(ACCESS_TOKEN_ID, String.class);
    }

    public String getRefreshTokenIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().get(REFRESH_TOKEN_ID, String.class);
    }

    public String getReferenceFromRefreshJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().get(REFERENCE, String.class);
    }

    public String getJwtTokenType(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody();
        return claims.containsKey(ACCESS_TOKEN_ID) ? ACCESS_TOKEN_ID : claims.containsKey(REFRESH_TOKEN_ID) ? REFRESH_TOKEN_ID : null;
    }

    public String getDeviceIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token).getBody().get(DEVICE_ID, String.class);
    }

    public String encodeDeviceId(String deviceId) {
        return passwordEncoder.encode(deviceId);
    }

    public boolean checkRawAgainstEncoded(String rawDeviceId, String encodedDeviceId) {
        return passwordEncoder.matches(rawDeviceId, encodedDeviceId);
    }

    public boolean validateJwtTokenAndDeviceId(String jwtToken, String deviceHeader){
        if (!checkRawAgainstEncoded(deviceHeader, getDeviceIdFromJwtToken(jwtToken))) {
            logger.error("Jwt device id and device id dont match");
            return false;
        }
        return true;
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(jwtToken);

            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}