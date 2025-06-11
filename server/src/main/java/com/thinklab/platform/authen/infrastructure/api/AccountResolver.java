package com.thinklab.platform.authen.infrastructure.api;

import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.authen.domain.model.LoginRequest;
import com.thinklab.platform.authen.domain.service.AuthenticationService;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class AccountResolver {

    @Autowired
    private final AuthenticationService service;

    @MutationMapping
    public String login(@Argument("input") LoginRequest request){
        try{
            Result<String, NotFoundException> login = service.login(request);
            if(login.isSuccess()){
                return login.getSuccessData();
            }
            throw new NotFoundException(login.getFailedData().getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }


    }

    @SneakyThrows
    @MutationMapping
    public String signUp(@Argument("input") CreateAccountRequest request){
        try{
            Result<String, InputsInvalidateException> signUp = service.signUp(request);
            if(signUp.isSuccess()){
                return signUp.getSuccessData();
            }
            throw new InputsInvalidateException(signUp.getFailedData().getMessage());
        }
        catch (InputsInvalidateException e){
            throw new InputsInvalidateException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }
}
