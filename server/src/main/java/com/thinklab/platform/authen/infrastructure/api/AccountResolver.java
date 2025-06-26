package com.thinklab.platform.authen.infrastructure.api;

import com.thinklab.platform.authen.domain.model.CreateAccountRequest;
import com.thinklab.platform.authen.domain.model.LoginRequest;
import com.thinklab.platform.authen.domain.service.AuthenticationService;
import com.thinklab.platform.authen.domain.service.HeaderUtils;
import com.thinklab.platform.share.domain.exception.InputsInvalidateException;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.exception.ValidateException;
import com.thinklab.platform.share.domain.model.Result;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private final HeaderUtils utils;

    @Autowired
    private final HttpServletRequest servletRequest;
    @MutationMapping
    public String login(@Argument("input") LoginRequest request){
        try{

            HeaderUtils.DeviceInfo deviceInfo = HeaderUtils.extractDeviceInfo(servletRequest);
            request.setIp(deviceInfo.ip());
            request.setDevice(deviceInfo.userAgent());
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


    private String extractClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isEmpty()) {
            return header.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
