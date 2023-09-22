package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShareFileReq {
    @JsonProperty("user_file_id")
    private String userFileId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("expire_time")
    private Long expireTime;
}
