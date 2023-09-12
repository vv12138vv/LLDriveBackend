package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.lang.NonNullFields;

import java.sql.Timestamp;

@Data
@TableName("users")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
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
    private boolean banned;
    @TableField(value = "is_admin")
    private boolean admin;
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    private Timestamp createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;
}
