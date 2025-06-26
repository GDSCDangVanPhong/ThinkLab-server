package com.thinklab.platform.user.infrastructure.repository_implement;

import com.mongodb.DuplicateKeyException;
import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.share.domain.service.EncryptionService;
import com.thinklab.platform.user.domain.repository_interface.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;



@Repository
@AllArgsConstructor
@Data
public class UserRepositoryImplement implements UserRepository {

    @Autowired
    private final MongoTemplate mongo;

    @Autowired
    private final EncryptionService encryptionService;

    @SneakyThrows
    public Result<UsersEntity, ValidateException> saveUser(AccountEntity account) {
        try {
            UsersEntity user = UsersEntity.covert(account);
            UsersEntity userProfile = mongo.save(user, "user");
            return Result.success(userProfile);
        } catch (DuplicateKeyException e) {
            return Result.failed(new ValidateException("User With This Email Already Exist!"));
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    @SneakyThrows
    public Result<UsersEntity, NotFoundException> deleteUser(ObjectId userID){
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
                return Result.failed(new NotFoundException("No User Found"));
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
