package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "user_files")
public class UserFile {
    @TableId(value ="id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_file_id")
    private String userFileId;
    @TableField(value = "is_dir")
    private Boolean isDir;
    @TableField(value = "dir_id")
    private String dirId;
    @TableField("file_id")
    private String fileId;
    @TableField("type")
    private String type;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "repo_id")
    private String repoId;
    @TableField(value = "is_deleted")
    private Boolean isDeleted;
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    private Timestamp createTime;
    @TableField(value = "update_time",fill=FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;


}
