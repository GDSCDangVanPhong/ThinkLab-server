package com.thinklab.platform.authen.domain.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String rawPassword;

}
