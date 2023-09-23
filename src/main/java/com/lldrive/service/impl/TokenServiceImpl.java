package com.lldrive.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lldrive.domain.entity.User;
import com.lldrive.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.lldrive.domain.consts.Const.*;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public String addToken(Type type,String key,Object value){
        String trueKey= type.name()+":"+key;
        int time=0;
        switch (type){
            case Login:
                time=LOGIN_VALID_TIME;
                break;
            default:
                time=CODE_VALID_TIME;
        }
        redisTemplate.opsForValue().set(trueKey,value,time, TimeUnit.MINUTES);
        return trueKey;
    }

    @Override
    public Object getValue(Type type, String key){
        String trueKey= type.name()+":"+key;
        return redisTemplate.opsForValue().get(trueKey);
    }

    @Override
    public boolean updateToken(Type type, String key) {
        return true;
    }

    @Override
    public User getUser(Type type, String key) {
        Object obj= getValue(type,key);
        if(obj==null){
            return null;
        }
        ObjectMapper mapper=new ObjectMapper();
        try {
            String jsonString=mapper.writeValueAsString(obj);
            User user=mapper.readValue(jsonString, User.class);
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
