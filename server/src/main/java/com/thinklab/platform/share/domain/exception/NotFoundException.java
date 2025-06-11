package com.thinklab.platform.share.domain.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException{

    public final String message;
    public final String code  ="NOT_FOUND";

    public NotFoundException(String message) {
        this.message =message;

    }
}
