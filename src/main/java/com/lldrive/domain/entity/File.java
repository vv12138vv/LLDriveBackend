package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Value;

import java.sql.Timestamp;

@Data
@TableName("files")
public class File {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
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
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time",update = "now()")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

}
