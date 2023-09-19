package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MoveFileReq {
    @NotBlank
    private String username;
    @NotBlank
    @JsonProperty("user_file_id")
    private String userFileId;
    @NotBlank
    @JsonProperty("dir_id")
    private String dirId;
}
