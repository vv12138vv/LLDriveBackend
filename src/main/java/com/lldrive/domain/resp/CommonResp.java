package com.lldrive.domain.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lldrive.domain.types.Status;
import lombok.Data;

@Data
public class CommonResp<T> {
    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("data")
    private T data;

    public CommonResp(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
    public CommonResp(Status status){
        this.statusCode=status.getCode();
        this.statusMsg=status.getMsg();
    }
    public CommonResp(Status status,T data){
        this.statusCode=status.getCode();
        this.statusMsg=status.getMsg();
        this.data=data;
    }
}
