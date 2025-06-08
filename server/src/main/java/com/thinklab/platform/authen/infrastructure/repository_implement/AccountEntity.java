package com.thinklab.platform.authen.infrastructure.repository_implement;

import com.thinklab.platform.authen.domain.model.AccountProvider;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "account")
@Data
public class AccountEntity {

    @Indexed
    private UUID accountID;
    @Indexed(unique = true)
    private final String email;
    private String username;
    private final String hashedPassword;
    private String resetPasswordToken;
    private String mfaSecret;
    private boolean mfaEnabled;
    private boolean verified;
    private AccountProvider provider;

}
