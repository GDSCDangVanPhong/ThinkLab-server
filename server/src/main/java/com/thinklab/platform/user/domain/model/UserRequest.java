package com.thinklab.platform.user.domain.model;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.user.infrastructure.repository_implement.UsersEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserRequest extends UsersEntity {
    private UUID id;
    private final String email;
    private String username;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();
    private AccountProvider provider;

    @SneakyThrows
    public Result<Boolean, ValidateException> validateResult(){
        try{
            if(
                    email!=null &&
                    password!= null && password.length()>=8 &&
                    provider!=null
            ){
                return Result.success(true);
            }
            else{
                return Result.failed(new ValidateException("Invalid Inputs, Please Check Your Information Again!"));
            }

        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

}
