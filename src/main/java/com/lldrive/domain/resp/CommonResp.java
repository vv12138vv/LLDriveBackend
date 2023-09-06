package com.lldrive.domain.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommonResp {
    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
}
