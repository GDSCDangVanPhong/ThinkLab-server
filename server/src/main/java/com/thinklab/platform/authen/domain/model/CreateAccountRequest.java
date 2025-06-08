package com.thinklab.platform.authen.domain.model;

import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class CreateAccountRequest {
    private String email;
    private String password;
    private String username;
    private AccountProvider provider;

    @SneakyThrows
    public Result<Boolean, InputsInvalidateException> validateRequest(){
        try{
            if(email!=null && password.length()>=8 && username!=null){
                return Result.success(true);
            }
            else{
                return Result.failed(new InputsInvalidateException("Invalid Inputs, Please Check Again!"));
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

}
