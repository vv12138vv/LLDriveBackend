package com.lldrive.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.req.*;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.resp.UserInfoResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.EmailService;
import com.lldrive.service.TokenService;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    @Autowired
    private TokenService tokenService;
    @Override
    public CommonResp register(RegisterReq registerReq) {
        if(userMapper.selectByUsername(registerReq.getUsername())!=null){//用户名检验
            return new CommonResp(Status.USERNAME_EXIST);
        }
        if(userMapper.selectByEmail(registerReq.getEmail())!=null){//邮箱检验
            return new CommonResp(Status.EMAIL_EXIST);
        }
        String code=(String) tokenService.getValue(TokenService.Type.Regsiter,registerReq.getEmail());
        String reqCode=registerReq.getCode().toString().toLowerCase(Locale.ROOT);
        if(!code.equals(reqCode)){//验证码检验
            return new CommonResp(Status.INCORRECT_CODE);
        }
        User user=new User(registerReq);
        int res=userMapper.insert(user);
        if(res==1){
            return new CommonResp(Status.SUCCESS,user);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp sendRegisterCode(String email){
        User user=userMapper.selectByEmail(email);
        if(user!=null){
            return new CommonResp(Status.EMAIL_EXIST);
        }
        //生成6位验证码
        String code=UUIDUtil.generate(6);
        //5 min有效期，存入redis
//        redisTemplate.opsForValue().set(email,code,CODE_VALID_TIME,TimeUnit.MINUTES);
        tokenService.addToken(TokenService.Type.Regsiter,email,code);
        //生成邮件
        String content=String.format(Email.USER_REGISTER_CONTENT,CODE_VALID_TIME,code);
        return emailService.sendEmail(email,Email.USER_REGISTER_SUBJECT,content);
    }

    @Override
    public CommonResp sendResetCode(String email) {
        User user=userMapper.selectByEmail(email);
        if(user==null){
            return new CommonResp(Status.EMAIL_NOT_EXIST);
        }
        String code=UUIDUtil.generate(6);
        tokenService.addToken(TokenService.Type.Reset,email,code);
        String content=String.format(Email.RESET_PASSWORD_CONTENT,CODE_VALID_TIME,code);
        return emailService.sendEmail(email,Email.RESET_PASSWORD_SUBJECT,content);
    }

    @Override
    public CommonResp login(LoginReq loginReq) {
        User user = userMapper.selectByEmail(loginReq.getEmail());
        if (user == null) {//用户名不存在
            return new CommonResp(Status.EMAIL_NOT_EXIST);
        }
        if (!user.getPassword().equals(loginReq.getPassword())) {
            return new CommonResp(Status.INCORRECT_PASSWORD);
        }

        //存入redis
        String key = UUIDUtil.generate(UUID_LENGTH);
        user.setPassword(null);
//        redisTemplate.opsForValue().set(key, user, LOGIN_VALID_TIME, TimeUnit.MINUTES);
        tokenService.addToken(TokenService.Type.Login,key,user);
        //返回token
        Map<String, Object> data = new HashMap<>();
        data.put("token", key);
        return new CommonResp(Status.SUCCESS, data);
    }

    @Override
    public CommonResp resetPassword(ResetPasswordReq resetPasswordReq) {
        User user=userMapper.selectByUsername(resetPasswordReq.getUsername());
        if(user==null){
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
//        String email=(String) redisTemplate.opsForValue().get(user.getEmail());
        String code=(String) tokenService.getValue(TokenService.Type.Reset,user.getEmail());
        String reqCode=resetPasswordReq.getCode().toString().toLowerCase(Locale.ROOT);
        if(!code.equals(reqCode)){//验证码检验
            return new CommonResp(Status.INCORRECT_CODE);
        }
        return new CommonResp(Status.SUCCESS);
    }

    @Override
    public CommonResp setNewPasword(SetNewPasswordReq setNewPasswordReq) {
        int res=userMapper.updatePassword(setNewPasswordReq.getUsername(),setNewPasswordReq.getNewPassword());
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp getUserInfo(String token) {
        User user=tokenService.getUser(TokenService.Type.Login,token);
        if(user==null){
            return new CommonResp(Status.USERNAME_NOT_EXIST);
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

    @Override
    public CommonResp findUser(String username){
        User user=userMapper.selectByUsername(username);
        if(user==null){
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
        return new CommonResp(Status.SUCCESS,user);
    }

    @Override
    public CommonResp getUsersInfo(Integer current,Integer size) {
        Page<User> page=new Page<>(current,size);
        IPage iPage=userMapper.selectPage(page,null);
        return new CommonResp(Status.SUCCESS,iPage);
    }

    @Override
    public CommonResp changePassword(ChangePasswordReq changePasswordReq) {
        User user=userMapper.selectByUsername(changePasswordReq.getUsername());
        if(user==null){
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
        if(!user.getPassword().equals(changePasswordReq.getPassword())){
            return new CommonResp(Status.INCORRECT_PASSWORD);
        }
        return new CommonResp(Status.SUCCESS,user);
    }
}
