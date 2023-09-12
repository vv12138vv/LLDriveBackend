package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("user_files")
public class UserFile {
    @TableId(value ="id")
    private long id;
    @TableField(value = "user_file_id")
    private String userFileId;
    @TableField(value = "is_dir")
    private boolean dir;
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
    private boolean deleted;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time",update = "now()")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

}