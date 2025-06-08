package com.thinklab.platform.share.domain.exception;

import lombok.Data;

@Data
public class InputsInvalidateException extends Exception{
    private final String code =  "INVALID_INPUT";
    private String msg ;

    public InputsInvalidateException(String message){
        this.msg = message;
    }

}
