package com.lldrive.service.impl;

import com.lldrive.domain.req.RegisterReq;
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
}
