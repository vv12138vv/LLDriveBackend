package com.lldrive.service;

import com.lldrive.domain.req.RegisterReq;
import com.lldrive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UserService {
    void addUser(RegisterReq registerReq);
    void sendEmailCode(String email);
}
