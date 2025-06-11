package com.thinklab.platform.authen.domain.repository_interface;

import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.model.Result;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepositoryInterface {
    public Result<AccountEntity, InputsInvalidateException> saveAccount(CreateAccountRequest request, String hashedPass);

    Result<AccountEntity, NotFoundException> getAccountByEmail(String email);

    Result<AccountEntity, NotFoundException> deleteAccount(UUID id);
}
