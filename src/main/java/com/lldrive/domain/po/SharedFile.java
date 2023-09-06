package com.lldrive.domain.po;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SharedFile {
    private long id;
    private String sharedId;
    private String userFileId;
    private String code;
    private Timestamp share_time;
    private Timestamp expire_time;
}
