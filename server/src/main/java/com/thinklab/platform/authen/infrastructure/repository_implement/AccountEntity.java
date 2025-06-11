package com.thinklab.platform.authen.infrastructure.repository_implement;

import com.thinklab.platform.authen.domain.model.AccountProvider;
import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "account")
@Data
@Builder
public class AccountEntity {

    @Id
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

    @SneakyThrows
    public static Result<AccountEntity,InputsInvalidateException> convert(CreateAccountRequest request, String hashedPassword){
        try{
            Result<Boolean, InputsInvalidateException> validate = request.validateRequest();
            if(!validate.isSuccess()){
                throw validate.getFailedData();
            }
            return Result.success(
                    AccountEntity.builder()
                            .accountID(UUID.randomUUID())
                            .email(request.getEmail())
                            .username(request.getUsername())
                            .hashedPassword(hashedPassword)
                            .resetPasswordToken(null)
                            .mfaSecret(null)
                            .mfaEnabled(false)
                            .provider(request.getProvider())
                            .build()
            );
        }

        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }



}
