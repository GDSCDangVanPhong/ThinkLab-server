package com.thinklab.platform.user.infrastructure.repository_implement;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.share.domain.service.EncryptionService;
import com.thinklab.platform.user.domain.model.UserRequest;
import com.thinklab.platform.user.domain.repository_interface.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
@Data
public class UserRepositoryImplement implements UserRepository {

    @Autowired
    private final MongoTemplate mongo;

    @Autowired
    private final EncryptionService encryptionService;

    @SneakyThrows
    public Result<UserRequest, ValidateException> saveUser(UserRequest request) {
        try {
            if (request.validateResult().isSuccess()) {
                request.setPassword(encryptionService.encrypt(request.getPassword()).getSuccessData());
                UserRequest user = mongo.save(request);
                return Result.success(user);
            } else {
                return Result.failed(request.validateResult().getFailedData());
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    @SneakyThrows
    public Result<UsersEntity, NotFoundException> deleteUser(UUID userID){
        try{
            Query query = Query.query(Criteria.where("_id").is(userID));
            UsersEntity user = mongo.findAndRemove(query, UsersEntity.class);
            if(user == null){
                return Result.failed(new NotFoundException("No Account Found"));
            }
            else{
                return Result.success(user);
            }
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }



    @SneakyThrows
    public Result<UsersEntity, NotFoundException> findUserByEmail(String email) {
        try {
            Query query = Query.query(Criteria.where("email").is(email));
            UsersEntity user = mongo.findOne(query, UsersEntity.class);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.failed(new NotFoundException("No user found"));
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }


//    @SneakyThrows
//    public Result<UsersEntity,NotFoundException> modifyAccount(UserRequest request ){
//        try{
//            if(request.getId()!= null){
//                Query query = Query.query(Criteria.where("_id").is(request.getId()));
//                Update update = new Update();
//                update.set()
//            }
//        }
//    }

}
