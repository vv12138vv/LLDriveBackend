package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("repos")
public class Repo {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "repo_id")
    private String repoId;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "max_capacity")
    private double maxCapacity;
    @TableField(value = "cur_capacity")
    private double curCapacity;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value="update_time",update = "now")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;
}
