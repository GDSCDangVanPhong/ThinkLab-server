package com.thinklab.platform.user.infrastructure.repository_implement;


import com.thinklab.platform.user.domain.model.AccountStatus;
import com.thinklab.platform.user.domain.model.SubscriptionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class UsersEntity {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String email;

    private String username;

    private String hashedPassword;

    private String fullName;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    private AccountStatus status;

    private SubscriptionInfo subscriptionInfo;
}
