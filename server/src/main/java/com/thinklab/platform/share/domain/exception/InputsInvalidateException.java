package com.thinklab.platform.share.domain.exception;

import lombok.Data;

@Data
public class InputsInvalidateException extends RuntimeException{
    private final String code =  "INVALID_INPUT";
    private String message ;

    public InputsInvalidateException(String message){
        this.message = message;
    }

}
