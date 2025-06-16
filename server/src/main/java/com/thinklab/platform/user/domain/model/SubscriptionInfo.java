package com.thinklab.platform.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder

public class SubscriptionInfo {
    private String planName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isSponsored;
    private boolean autoRenew;
    private String billingCycle;

}




