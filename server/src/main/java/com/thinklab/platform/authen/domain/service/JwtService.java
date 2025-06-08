package com.thinklab.platform.authen.domain.service;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
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
        if (secretKey == null) {
            throw new IllegalStateException("ENCRYPTION_KEY is not configured");
        }
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }


    @SneakyThrows
    public Result<String, InternalErrorException> generateToken(UUID userID) {
        try {
            return Result.success(
                    Jwts.builder()
                            .subject("sessionID")
                            .claim("sessionID", userID)
                            .issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + expireDate))
                            .signWith(key)
                            .compact()
            );
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
