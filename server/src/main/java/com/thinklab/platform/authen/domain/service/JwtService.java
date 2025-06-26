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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@Data
public class JwtService {

    private final long expireDate = 60 * 1000 * 24 ;

    @Value("${ENCRYPTION_KEY}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            byte[] keyBytes = new byte[32];
            new SecureRandom().nextBytes(keyBytes);
            this.key = Keys.hmacShaKeyFor(keyBytes);
            this.secretKey = Base64.getEncoder().encodeToString(keyBytes);
        } else {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            if (keyBytes.length < 32) {
                throw new IllegalStateException("ENCRYPTION_KEY must be at least 256 bits (32 bytes) after Base64 decoding");
            }
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }



    public String generateToken(ObjectId userID, Duration ttl) {
        try {
            long now = System.currentTimeMillis();
            long expirationMillis = ttl.toMillis();
            return
                    Jwts.builder()
                            .subject("sessionID")
                            .claim("sessionID", userID.toString())
                            .issuedAt(new Date(now))
                            .expiration(new Date(now + expirationMillis))
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
