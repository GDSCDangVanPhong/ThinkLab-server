package com.thinklab.platform.authen.domain.service;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HeaderUtils {

    public static DeviceInfo extractDeviceInfo(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .filter(h -> !h.isBlank() && !"unknown".equalsIgnoreCase(h))
                .map(h -> h.split(",")[0].trim())
                .orElse(request.getRemoteAddr());

        String userAgent = Optional.ofNullable(request.getHeader("User-Agent")).orElse("Unknown");
        return new DeviceInfo(ip, userAgent);
    }

    public record DeviceInfo(String ip, String userAgent) {}
}


