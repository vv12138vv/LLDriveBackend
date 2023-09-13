package com.lldrive.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.resp.UserInfoResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addUser(RegisterReq registerReq) {

    }

    @Override
    public void sendEmailCode(String Email) {

    }

    @Override
    public CommonResp login(LoginReq loginReq) {
        User user = userMapper.selectByUsername(loginReq.getUsername());
        if (user == null) {//用户名不存在
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
        if (!user.getPassword().equals(loginReq.getPassword())) {
            return new CommonResp(Status.INCORRECT_PASSWORD);
        }

        //存入redis
        String key = "user:" + UUIDUtil.generate(32);
        user.setPassword(null);
        redisTemplate.opsForValue().set(key, user, 5, TimeUnit.MINUTES);
        //返回token
        Map<String, Object> data = new HashMap<>();
        data.put("token", key);
        return new CommonResp(Status.SUCCESS, data);
    }

    @Override
    public CommonResp getUserInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj == null) {
            return new CommonResp(Status.LOGIN_EXPIRED);
        }
        //json转object
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            String jsonString = mapper.writeValueAsString(obj);
            user = mapper.readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //封装信息
        UserInfoResp resp=new UserInfoResp(user);
        return new CommonResp(Status.SUCCESS,resp);
    }
}
