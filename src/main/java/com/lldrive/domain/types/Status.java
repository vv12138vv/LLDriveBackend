package com.lldrive.domain.types;

public enum Status{
    SYSTEM_ERROR(1000,"系统异常"),
    REQUEST_PARAMS_ERROR(1001,"参数错误"),

    USERNAME_EXIST(4000,"用户名已存在"),
    EMAIL_EXIST(4001,"邮箱已存在");


    private Integer statusCode;
    private String statusMsg;

    public Integer getCode() {
        return statusCode;
    }
    public String getMsg() {
        return statusMsg;
    }
    Status(Integer code, String msg){
        statusCode=code;
        statusMsg=msg;
    }

}
