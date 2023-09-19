package com.lldrive.service;

import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.req.ResetPasswordReq;
import com.lldrive.domain.req.SetNewPasswordReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UserService {
    CommonResp register(RegisterReq registerReq);

    CommonResp login(LoginReq loginReq);
    CommonResp sendRegisterCode(String email);
    CommonResp sendResetCode(String email);
    CommonResp getUserInfo(String token);
    CommonResp logout(String token);

    CommonResp resetPassword(ResetPasswordReq resetPasswordReq);
    CommonResp setNewPasword(SetNewPasswordReq setNewPasswordReq);
    CommonResp findUser(String username);

    CommonResp getUsersInfo(Integer curreent,Integer size);
}
