package com.lldrive.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("chunks")
public class Chunk {
    @TableId
    private Integer id;
    @TableField("chunk_id")
    private String chunkId;
    @TableField("chunk_number")//当前分片序号
    private Integer chunkNumber;
    @TableField("chunk_size")
    private Long chunkSize;//分片大小
    @TableField("cur_chunk_size")
    private Long currentChunkSize;//当前分片大小
    @TableField("total_size")
    private Long totalSize;//总大小
    @TableField("total_chunks")
    private Integer totalChunks;//总分片数
    @TableField("upload_time")
    private Timestamp uploadTime;//上传时间
}
