package com.thinklab.platform.user.infrastructure.repository_implement;


import com.thinklab.platform.authen.domain.model.AccountProvider;
import com.thinklab.platform.user.domain.model.SubscriptionPlan;
import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.InternalErrorException;

import com.thinklab.platform.user.domain.model.AccountStatus;
import com.thinklab.platform.user.domain.model.BillingCycle;
import com.thinklab.platform.user.domain.model.SubscriptionInfo;

import lombok.Builder;
import lombok.Data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "user")
public class UsersEntity {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String email;

    private String username;

    private String fullName;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private AccountStatus status;

    private SubscriptionInfo subscriptionInfo;

    public static UsersEntity covert (AccountEntity account){
        try{
            return
                    UsersEntity.builder()
                            .id(account.getAccountID())
                            .avatarUrl("")
                            .username(account.getUsername())
                            .createdAt(LocalDateTime.now())
                            .status( account.getProvider()!= AccountProvider.SYSTEM ?
                                    AccountStatus.ACTIVE : AccountStatus.PENDING_VERIFICATION)
                            .subscriptionInfo(
                                    SubscriptionInfo.builder()
                                            .plan(SubscriptionPlan.HOBBY)
                                            .startDate(LocalDateTime.now())
                                            .endDate(LocalDateTime.of(9999, 12,31,23,59,59))
                                            .autoRenew(false)
                                            .isSponsored(false)
                                            .billingCycle(BillingCycle.NONE)
                                            .stripeCustomerId("")
                                            .stripePriceId("")
                                            .stripeSubscriptionId("")
                                            .build()
                            )
                            .build();
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }
}
