package com.lldrive.domain.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Repo {
    private long id;
    private String repoId;
    private String userId;
    private double maxCapacity;
    private double curCapacity;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
}
