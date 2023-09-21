package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
public class UploadFileReq {
    @NotBlank
    private String username;
    private Boolean dir;//是否是文件夹
    private String dirId;//父文件夹的user_file_id
    @NotNull
    private Boolean chunkFlag;//是否分片
    private Integer chunkNumber;
    @NotBlank
    private String fileName;
    @NotNull
    private Integer totalChunks;//切片总数
    @NotNull
    private Integer totalSize;//文件总大小
    @NotBlank
    private String hash;//切片MD5值
    private MultipartFile file;//文件
}
