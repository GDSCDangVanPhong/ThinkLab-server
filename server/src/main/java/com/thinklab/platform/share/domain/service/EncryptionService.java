package com.thinklab.platform.share.domain.service;



import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.SneakyThrows;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service

public class EncryptionService {

    @SneakyThrows
    public String encrypt(String rawText){
        try{
            return BCrypt.hashpw(rawText, BCrypt.gensalt());
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    @SneakyThrows
    public Result<Boolean, ValidateException> checkEqual(String rawText, String encryptedText, String msg){
        try{
            if(BCrypt.checkpw(rawText,encryptedText)){
                return Result.success(true);
            }
            else {
                return Result.failed(new ValidateException(msg));
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }

    }

}
