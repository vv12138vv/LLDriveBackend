package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("shared_files")
public class SharedFile {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "shared_id")
    @JsonProperty("shared_id")
    private String sharedId;
    @TableField(value="user_file_id")
    @JsonProperty("user_file_id")
    private String userFileId;
    @TableField(value ="code")
    private String code;
    @TableField(value = "shared_count")
    @JsonProperty("shared_count")
    private Long sharedCount;
    @TableField(value ="share_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonProperty("share_time")
    private Timestamp shareTime;
    @TableField("file_name")
    @JsonProperty("file_name")
    private String fileName;
    @TableField("type")
    private Integer type;
    @TableField(value ="expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonProperty("expire_time")
    private Timestamp expireTime;
}
