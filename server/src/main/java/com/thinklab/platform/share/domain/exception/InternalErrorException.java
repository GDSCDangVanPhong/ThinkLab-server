package com.thinklab.platform.share.domain.exception;

import lombok.Getter;

@Getter
public class InternalErrorException extends RuntimeException {
    public final String code = "INTERNAL_ERROR";
    public final String message ;


    public InternalErrorException(String message){
        this.message = message;
    }

}
