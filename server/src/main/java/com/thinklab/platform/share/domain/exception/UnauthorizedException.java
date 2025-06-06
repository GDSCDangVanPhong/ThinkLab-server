package com.thinklab.platform.share.domain.exception;

public class UnauthorizedException extends RuntimeException {
    private final String message;
    private final String code = "UNAUTHORIZED";
    public UnauthorizedException( String message){
        this.message = message;
    }

}
