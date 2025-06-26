package com.thinklab.platform.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionInfo {
    private SubscriptionPlan plan;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isSponsored;
    private boolean autoRenew;
    private BillingCycle billingCycle;

    private String stripeSubscriptionId;
    private String stripeCustomerId;
    private String stripePriceId;

}




