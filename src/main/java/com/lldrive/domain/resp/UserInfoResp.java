package com.lldrive.domain.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lldrive.domain.entity.User;
import lombok.Data;

@Data
public class UserInfoResp {
    private String username;
    private String email;
    @JsonProperty("is_banned")
    private Boolean isBanned;
    @JsonProperty("is_admin")
    private Boolean isAdmin;

    public UserInfoResp(User user){
        this.username=user.getUsername();
        this.email=user.getEmail();
        this.isBanned =user.getIsBanned();
        this.isAdmin =user.getIsAdmin();
    }
}
