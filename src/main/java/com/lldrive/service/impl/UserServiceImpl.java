package com.lldrive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void addUser(RegisterReq registerReq) {

    }

    @Override
    public void sendEmailCode(String Email){

    }

    @Override
    public CommonResp login(LoginReq loginReq) {
//        LambdaQueryWrapper<User>wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername,loginReq.getUsername());
//        wrapper.eq(User::getPassword,loginReq.getPassword());
//        User user=userMapper.selectOne(wrapper);
        User user=userMapper.selectByUsername(loginReq.getUsername());
        if(user==null){//用户名不存在
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
        if(!user.getPassword().equals(loginReq.getPassword())) {
            return new CommonResp(Status.INCORRECT_PASSWORD);
        }
        return new CommonResp(Status.SUCCESS);
    }
}
