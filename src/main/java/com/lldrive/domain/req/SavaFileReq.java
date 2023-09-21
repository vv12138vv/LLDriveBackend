package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SavaFileReq {
    @JsonProperty("shared_id")
    private String sharedId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("dir_id")
    private String dirId;
}
