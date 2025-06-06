package com.thinklab.platform.share.domain.exception;

public class InternalErrorException extends Exception {
    public final String code = "INTERNAL_ERROR";
    public final String message ;


    public InternalErrorException(String message){
        this.message = message;
    }

}
