package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MkDirReq {
    @JsonProperty("username")
    @NotBlank
    private String username;
    @JsonProperty("dir_id")
    private String dirId;
    @JsonProperty("dir_name")
    @NotBlank
    private String dirName;
}
