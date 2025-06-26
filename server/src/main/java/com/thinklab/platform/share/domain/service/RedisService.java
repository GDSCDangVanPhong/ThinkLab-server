package com.thinklab.platform.share.domain.service;

import com.thinklab.platform.share.domain.exception.InternalErrorException;
import com.thinklab.platform.share.domain.exception.NotFoundException;
import com.thinklab.platform.share.domain.model.Result;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key , Object value){
        try{
            redisTemplate.opsForValue().set(key,value);
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }


    public Result<Object, NotFoundException> readData(String key){
        try{
           Object data =  redisTemplate.opsForValue().get(key);
           if(data == null){
               return Result.failed(new NotFoundException("No Data Exist!"));
           }
           return Result.success(data);
        }
        catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

}
