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
    public Result<CreateAccountRequest, InputsInvalidateException> saveAccount(CreateAccountRequest request){
        try{
            Result<Boolean, InputsInvalidateException> validateInput = request.validateRequest();
            if(validateInput.isSuccess()){
                CreateAccountRequest save = mongo.save(request);
                return Result.success(save);
            }
            else {
                return Result.failed(validateInput.getFailedData());
            }
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
