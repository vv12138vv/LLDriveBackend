package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListRecycleReq {
    @JsonProperty("username")
    private String username;
    @JsonProperty("page_no")
    private String pageNo;
    @JsonProperty("page_size")
    private String pageSize;
}
