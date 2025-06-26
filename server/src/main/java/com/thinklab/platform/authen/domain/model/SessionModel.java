package com.thinklab.platform.authen.domain.model;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionModel {
    private String refreshToken;
    private String device;
    private String ip;
    private LocalDateTime createdAt;

    public static SessionModel convert(String ip, String device, String token){
        try {
            return (
                    SessionModel.builder()
                            .refreshToken(token)
                            .device(device)
                            .ip(ip)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }

    }

}
