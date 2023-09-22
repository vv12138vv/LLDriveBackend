package com.lldrive.service.impl;

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
    public void  addToken(Type type,String key,Object value){
        String trueKey= type.name()+":"+key;
        redisTemplate.opsForValue().set(trueKey,value,CODE_VALID_TIME, TimeUnit.MINUTES);
    }
    public Object getToken(Type type,String key){
        String trueKey= type.name()+":"+key;
        return redisTemplate.opsForValue().get(trueKey);
    }
}
