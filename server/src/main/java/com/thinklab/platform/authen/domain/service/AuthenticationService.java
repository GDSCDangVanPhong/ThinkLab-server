package com.thinklab.platform.authen.domain.service;

import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.authen.domain.model.LoginRequest;
import com.thinklab.platform.authen.domain.repository_interface.AccountRepositoryInterface;
import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.share.domain.service.EncryptionService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthenticationService {

    @Autowired
    private final AccountRepositoryInterface accountRepo;

    @Autowired
    private final EncryptionService encryptionService;

    @Autowired
    private final JwtService jwtService;

    @SneakyThrows
    public Result<String, NotFoundException> login(LoginRequest request) {
        try {
            Result<AccountEntity, NotFoundException> account = accountRepo.getAccountByEmail(request.getEmail());
            if (!account.isSuccess()) {
                return Result.failed(account.getFailedData());
            } else {
                Result<Boolean, ValidateException> result = encryptionService.checkEqual(request.getRawPassword(),
                        account.getSuccessData().getHashedPassword(),
                        "Wrong Password!");
                if (result.isSuccess()) {
                    Result<String, InternalErrorException> response = jwtService.
                            generateToken(account.getSuccessData().getAccountID());
                    return Result.success(response.getSuccessData());
                } else {
                    throw result.getFailedData();
                }
            }
        } catch (ValidateException e) {
            throw new ValidateException(e.getMessage());
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    @SneakyThrows
    public Result<String , ValidateException> signUp(CreateAccountRequest request){
        try{
            Result<CreateAccountRequest, InputsInvalidateException> result = accountRepo.saveAccount(request);
            if (result.isSuccess()){
                Result<String, InternalErrorException> token = jwtService.generateToken(request)
                return Result.success()
            }
        }
    }



}
