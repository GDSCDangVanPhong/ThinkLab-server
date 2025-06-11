package com.thinklab.platform.share.domain.exception;

import lombok.Data;

@Data
public class UnauthorizedException extends RuntimeException {
    private final String message;
    private final String code = "UNAUTHORIZED";
    public UnauthorizedException( String message){
        this.message = message;
    }

}
