package com.thinklab.platform.share.domain.service;


import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.SneakyThrows;
import lombok.Value;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service

public class EncryptionService {


    @SneakyThrows
    public Result<String, InternalError> encrypt(String rawText){
        try{
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            String output = encryptor.encryptPassword(rawText);
            return Result.success(output);

        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    @SneakyThrows
    public Result<Boolean, ValidateException> checkEqual(String rawText, String encryptedText , String msg){
        try{
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            if(encryptor.checkPassword(rawText,encryptedText)){
                return Result.success(true);
            }
            else{
                return Result.failed(new ValidateException(msg));
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }


}
