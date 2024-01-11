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
    @TableField("hash")
    private String hash;
    @TableField("chunk_number")//当前分片序号
    private Integer chunkNumber;
    @TableField("cur_chunk_size")
    private Long currentChunkSize;//当前分片大小
    @TableField("is_upload")
    private Boolean isUpload;
    @TableField("chunk_size")
    private Integer chunkSize;
    @TableField("total_chunk")
    private Integer totalChunk;
    @TableField("file_id")
    private String fileId;
}
