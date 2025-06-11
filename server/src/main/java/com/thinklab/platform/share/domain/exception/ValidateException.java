package com.thinklab.platform.share.domain.exception;

import lombok.Data;

@Data
public class ValidateException extends RuntimeException{

    public final String message;
    public final String code  ="VALIDATE_FAILED";

    public ValidateException(String message){
        this.message = message;

    }
}
