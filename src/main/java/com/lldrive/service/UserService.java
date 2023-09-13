package com.lldrive.service;

import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UserService {
    CommonResp register(RegisterReq registerReq);

    CommonResp login(LoginReq loginReq);
    CommonResp sendEmailCode(String email);
    CommonResp getUserInfo(String token);
    CommonResp logout(String token);
}
