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
import com.thinklab.platform.user.domain.repository_interface.UserRepository;
import com.thinklab.platform.user.infrastructure.repository_implement.UsersEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final SessionService session;

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final HttpServletResponse response;

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
                    Cookie cookie = new Cookie("sessionId",account.getSuccessData().getAccountID().toString());
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setMaxAge(7 * 24 * 60 * 60);
                    response.addCookie(cookie);
                    return  Result.success(
                            session.createSession(account.getSuccessData().getAccountID(),
                                    request.getIp(),
                                    request.getDevice())
                    );

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
    public Result<String , InputsInvalidateException> signUp(CreateAccountRequest request){
        try{
            String hashedPassword = encryptionService.encrypt(request.getPassword());
            Result<AccountEntity, InputsInvalidateException> result1= accountRepo
                    .saveAccount(request,hashedPassword);
            if (!result1.isSuccess()) {
                return Result.failed(result1.getFailedData());
            }
            Result<UsersEntity, ValidateException> result2 = userRepo
                    .saveUser(result1.getSuccessData());
            if(!result2.isSuccess()){
                return Result.failed(new InputsInvalidateException(
                        "User Profile Creation Failed:" +
                                result2.getFailedData().getMessage()
                ));
            }
            Cookie cookie = new Cookie("sessionId", result2.getSuccessData().getId().toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            return Result.success(
                    session.createSession(
                            result2.getSuccessData().getId(),
                            request.getIp(),
                            request.getDevice()

                    )
            );

        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }



}
