package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Value;

import java.sql.Timestamp;

@Data
@TableName("files")
public class File {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "file_id")
    private String fileId;
    @TableField(value="hash")
    private String hash;
    @TableField(value = "type")
    private String type;
    @TableField(value = "size")
    private double size;
    @TableField(value ="path")
    private String path;
    @TableField(value = "count")
    private long count;
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    private Timestamp createTime;
    @TableField(value = "update_time",fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

}
