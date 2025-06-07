package com.thinklab.platform.authen.domain.service;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@Data
public class JwtService {

    private final long expireDate = 60*1000*24*7;

    @Value("${ENCRYPTION_KEY}")
    private String secretKey ;
    private SecretKey key;
    @PostConstruct
    public void init() {
        if (secretKey == null) {
            throw new IllegalStateException("ENCRYPTION_KEY is not configured");
        }
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }



    @SneakyThrows
    public Result<String, InternalErrorException> generateToken(UUID userID){
        try{
            return Result.success(
                    Jwts.builder()
                            .subject("sessionID")
                            .claim("sessionID", userID )
                            .issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis()+ expireDate))
                            .signWith(key)
                            .compact()
            );
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }


    @SneakyThrows
    public Result<Boolean, ValidateException> validateToken(String token){
        try{

            Jwts.parser()
                    .decryptWith(key)
                    .build()
                    .parse(token);
            return Result.success(true);
        }
        catch (ExpiredJwtException e){
            throw new ValidateException(e.getMessage());
        }
    }






}
