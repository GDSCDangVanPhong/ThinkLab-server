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
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "account")
@Data
@Builder
public class AccountEntity {

    @Id
    private ObjectId accountID;
    @Indexed(unique = true)
    private final String email;
    private String username;
    private final String hashedPassword;
    private String resetPasswordToken;
    private List<String> authorizedIP;
    private String mfaSecret;
    private boolean mfaEnabled;

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
                            .email(request.getEmail())
                            .username(request.getUsername())
                            .hashedPassword(hashedPassword)
                            .resetPasswordToken(null)
                            .authorizedIP(new ArrayList<>())
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
