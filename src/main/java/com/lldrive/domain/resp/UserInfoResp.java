package com.lldrive.domain.resp;

import com.lldrive.domain.entity.User;
import lombok.Data;

@Data
public class UserInfoResp {
    private String username;
    private String email;
    private Boolean banned;
    private Boolean admin;

    public UserInfoResp(User user){
        this.username=user.getUsername();
        this.email=user.getEmail();
        this.banned=user.getIsBanned();
        this.admin=user.getIsAdmin();
    }
}
