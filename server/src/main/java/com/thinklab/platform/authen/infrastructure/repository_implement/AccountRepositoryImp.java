package com.thinklab.platform.authen.infrastructure.repository_implement;

import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.authen.domain.repository_interface.AccountRepositoryInterface;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Queue;
import java.util.UUID;

@Repository
@Data
public class AccountRepositoryImp implements AccountRepositoryInterface {

    @Autowired
    private final MongoTemplate mongo;

    @SneakyThrows
    public Result<AccountEntity, InputsInvalidateException> saveAccount(CreateAccountRequest request, String hashedPass){
        try{
            Result<AccountEntity,InputsInvalidateException> account = AccountEntity.convert(request,hashedPass);
            AccountEntity result = mongo.save(account.getSuccessData());
            if(!account.isSuccess()){
                return Result.failed(account.getFailedData());
            }
            return Result.success(result);
        }

        catch (DuplicateKeyException e){
            return Result.failed(new InputsInvalidateException("This Email Has Been Registered!"));
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }

    }


    @SneakyThrows
    public Result<AccountEntity, NotFoundException> getAccountByEmail(String email){
        try{
            Query query = new Query(Criteria.where("email").is(email));
            AccountEntity account = mongo.findOne(query, AccountEntity.class);
            if(account!=null){
                return Result.success(account);
            }
            else{
                return Result.failed(new NotFoundException("No Account Found!"));
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }



    @SneakyThrows
    public Result<AccountEntity, NotFoundException> deleteAccount(UUID id){
        try{
            Query query = new Query(Criteria.where("_id").is(id));
            AccountEntity account = mongo.findAndRemove(query, AccountEntity.class);
            if(account== null){
                return Result.failed(new NotFoundException("No Account Exist"));
            }
            else{
                return Result.success(account);
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }




}
