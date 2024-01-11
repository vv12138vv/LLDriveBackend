package com.lldrive.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lldrive.domain.entity.SharedFile;
import com.lldrive.domain.entity.User;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class SharedFileResp {
    @JsonProperty("shared_id")
    private String sharedId;
    @JsonProperty("user_file_id")
    private String userFileId;
    @JsonProperty("shared_count")
    private Long sharedCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonProperty("share_time")
    private Timestamp shareTime;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("valid_type")
    private Integer validType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonProperty("expire_time")
    private Timestamp expireTime;
    @JsonProperty("sharer_name")
    private String sharerName;

    public SharedFileResp(SharedFile sharedFile, User user){
        this.sharedId=sharedFile.getSharedId();
        this.userFileId=sharedFile.getUserFileId();
        this.sharedCount=sharedFile.getSharedCount();
        this.shareTime=sharedFile.getShareTime();
        this.fileName=sharedFile.getFileName();
        this.type=sharedFile.getType();
        this.validType=sharedFile.getValidType();
        this.expireTime=sharedFile.getExpireTime();
        this.sharerName=user.getUsername();
    }

}
