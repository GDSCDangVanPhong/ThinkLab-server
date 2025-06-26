package com.thinklab.platform.authen.domain.service;

import com.thinklab.platform.authen.domain.model.LoginRequest;
import com.thinklab.platform.authen.domain.model.SessionModel;
import com.thinklab.platform.authen.infrastructure.repository_implement.AccountEntity;
import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.model.Result;
import com.thinklab.platform.share.domain.service.RedisService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Data
public class SessionService {

    @Autowired
    private final RedisService redis;

    @Autowired
    private final JwtService jwt;

    public String createSession (ObjectId id , String ip, String device){
        try{
            String refreshToken = jwt.generateToken(id, Duration.ofDays(7));
            String accessToken = jwt.generateToken(id, Duration.ofHours(1));
            SessionModel session = SessionModel.convert(ip,device, refreshToken);
            redis.saveData(id.toString(), session );
            return accessToken;
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

}
