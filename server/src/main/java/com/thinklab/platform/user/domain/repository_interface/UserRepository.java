package com.thinklab.platform.user.domain.repository_interface;

import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.user.infrastructure.repository_implement.UsersEntity;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository {

    Result<UsersEntity, ValidateException> saveUser(AccountEntity account);
    Result<UsersEntity, NotFoundException> findUserByEmail(String email);
    public Result<UsersEntity, NotFoundException> deleteUser(ObjectId userID);

}
