package com.lldrive.domain.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private long id;
    private String userId;
    private String repoId;
    private String email;
    private String username;
    private String password;
    private boolean banned;
    private boolean admin;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
}
