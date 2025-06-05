package com.thinklab.platform.share.domain.exception;

public class ValidateException extends Exception{

    public final String message;
    public final String code  ="VALIDATE_FAILED";

    public ValidateException(String message){
        this.message = message;

    }
}
