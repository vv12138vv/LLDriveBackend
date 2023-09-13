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
import com.lldrive.service.EmailService;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.lldrive.domain.consts.Email;

import static com.lldrive.domain.consts.Const.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmailService emailService;
    @Override
    public CommonResp register(RegisterReq registerReq) {
        if(userMapper.selectByUsername(registerReq.getUsername())!=null){//用户名检验
            return new CommonResp(Status.USERNAME_EXIST);
        }
        if(userMapper.selectByEmail(registerReq.getEmail())!=null){//邮箱检验
            return new CommonResp(Status.EMAIL_EXIST);
        }
        String email=(String) redisTemplate.opsForValue().get(registerReq.getEmail());
        String reqEmail=registerReq.getCode().toString().toLowerCase(Locale.ROOT);
        if(!email.equals(reqEmail)){//验证码检验
            return new CommonResp(Status.INCORRECT_CODE);
        }
        User user=new User(registerReq);
        int res=userMapper.insert(user);
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp sendEmailCode(String email){
        User user=userMapper.selectByEmail(email);
        if(user!=null){
            return new CommonResp(Status.EMAIL_EXIST);
        }
        //生成6位验证码
        String code=UUIDUtil.generate(6);
        //5 min有效期，存入redis
        redisTemplate.opsForValue().set(email,code,CODE_VALID_TIME,TimeUnit.MINUTES);
        //生成邮件
        String content=String.format(Email.USER_REGISTER_CONTENT,CODE_VALID_TIME,code);
        return emailService.sendEmail(email,Email.USER_REGISTER_SUBJECT,content);
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
        String key = "user:" + UUIDUtil.generate(UUID_LENGTH);
        user.setPassword(null);
        redisTemplate.opsForValue().set(key, user, LOGIN_VALID_TIME, TimeUnit.MINUTES);
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

    @Override
    public CommonResp logout(String token) {
        redisTemplate.delete(token);
        return new CommonResp(Status.SUCCESS);
    }
}
