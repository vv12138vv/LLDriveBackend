package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("shared_files")
public class SharedFile {
    @TableId(value = "id")
    private long id;
    @TableField(value = "shared_id")
    private String sharedId;
    @TableField(value="user_file_id")
    private String userFileId;
    @TableField(value ="code")
    private String code;
    @TableField(value = "shared_count")
    private long sharedCount;
    @TableField(value ="share_time")
    private Timestamp share_time;
    @TableField(value ="expire_time")
    private Timestamp expire_time;
}
