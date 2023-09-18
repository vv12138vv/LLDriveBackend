package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.req.RegisterReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName("users")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "repo_id")
    private String repoId;
    @TableField(value = "email")
    private String email;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "is_banned")
    private Boolean isBanned;
    @TableField(value = "is_admin")
    private Boolean isAdmin;
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    private Timestamp createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

    public User(RegisterReq registerReq){
        this.userId=UUIDUtil.generate(32);
        this.repoId=UUIDUtil.generate(32);
        this.email=registerReq.getEmail();
        this.username= registerReq.getUsername();
        this.password= registerReq.getPassword();
        this.isBanned =false;
        this.isAdmin =false;
    }
}
