package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "user_files")
public class UserFile {
    @TableId(value ="id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_file_id")
    @JsonProperty("user_file_id")
    private String userFileId;
    @TableField(value = "is_dir")
    @JsonProperty("is_dir")
    private Boolean isDir;
    @TableField(value = "dir_id")
    @JsonProperty("dir_id")
    private String dirId;
    @TableField("file_id")
    @JsonProperty("file_id")
    private String fileId;
    @TableField("type")
    @JsonProperty("type")
    private Integer type;
    @TableField(value = "file_name")
    @JsonProperty(("file_name"))
    private String fileName;
    @TableField(value = "repo_id")
    @JsonProperty("repo_id")
    private String repoId;
    @TableField(value = "is_deleted")
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @TableField(value = "size")
    @JsonProperty("size")
    private Long size;
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp createTime;
    @TableField(value = "update_time",fill=FieldFill.INSERT_UPDATE)
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    @JsonProperty("delete_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp deleteTime;


}
