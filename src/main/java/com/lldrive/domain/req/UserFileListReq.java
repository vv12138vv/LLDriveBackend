package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserFileListReq {
    @JsonProperty("username")
    private String username;
    @JsonProperty("dir_id")
    private String dirId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("page_no")
    private String pageNo;
    @JsonProperty("page_size")
    private String pageSize;
}
