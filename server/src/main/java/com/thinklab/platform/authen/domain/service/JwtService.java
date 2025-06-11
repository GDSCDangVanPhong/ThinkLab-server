package com.thinklab.platform.authen.domain.service;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@Data
public class JwtService {

    private final long expireDate = 60 * 1000 * 24 * 7;

    @Value("${ENCRYPTION_KEY}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            // Generate a secure random key if none is provided
            byte[] keyBytes = new byte[32]; // 256-bit key for HS256
            new SecureRandom().nextBytes(keyBytes);
            this.key = Keys.hmacShaKeyFor(keyBytes);
            // Optionally, encode and log the key for configuration (in a secure environment)
            this.secretKey = Base64.getEncoder().encodeToString(keyBytes);
        } else {
            // Use the provided key, ensuring it's decoded correctly
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            if (keyBytes.length < 32) {
                throw new IllegalStateException("ENCRYPTION_KEY must be at least 256 bits (32 bytes) after Base64 decoding");
            }
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }


    @SneakyThrows
    public String generateToken(UUID userID) {
        try {
            return
                    Jwts.builder()
                            .subject("sessionID")
                            .claim("sessionID", userID)
                            .issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + expireDate))
                            .signWith(key)
                            .compact()
            ;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }


    @SneakyThrows
    public Result<UUID, ValidateException> validateToken(String token) {
        try {
            return Result.success(Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("sessionID", UUID.class)
            );

        } catch (Exception e) {
            throw new ValidateException(e.getMessage());
        }
    }


}
