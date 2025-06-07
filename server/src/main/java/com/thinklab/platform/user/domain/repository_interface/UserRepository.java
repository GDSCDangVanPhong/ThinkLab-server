package com.thinklab.platform.user.domain.repository_interface;

import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.user.domain.model.UserRequest;
import com.thinklab.platform.user.infrastructure.repository_implement.UsersEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository {

    public Result<UserRequest, ValidateException> saveUser(UserRequest request);
    public Result<UsersEntity, NotFoundException> findUserByEmail(String email);
    public Result<UsersEntity, NotFoundException> deleteUser(UUID userID);

}
