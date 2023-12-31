package com.lldrive.service;

import com.lldrive.domain.req.*;
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

    CommonResp changePassword(ChangePasswordReq changePasswordReq);

    CommonResp listAllUser(Integer pageNo,Integer pageSize);

    CommonResp changeUserStatus(String userId, Boolean isBanned);

    CommonResp changeCapacity(String userId, Long newCapacity);

    CommonResp listSearchAllUser(Integer pageNo,Integer pageSize,String username);
}
