package com.lldrive.domain.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class File {
    private long id;
    private String fileId;
    private String hash;
    private String type;
    private double size;
    private String path;
    private long count;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;

}
