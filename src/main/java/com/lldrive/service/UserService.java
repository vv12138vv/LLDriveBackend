package com.lldrive.service;

import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UserService {
    void addUser(RegisterReq registerReq);
    void sendEmailCode(String email);

    CommonResp login(LoginReq loginReq);

    CommonResp getUserInfo(String token);
}
