package com.thinklab.platform.share.domain.exception;

public class NotFoundException extends Exception{

    public final String message;
    public final String code  ="NOT_FOUND";

    public NotFoundException(String message) {
        this.message =message;

    }
}
