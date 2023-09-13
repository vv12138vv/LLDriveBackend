package com.lldrive.domain.types;

import com.lldrive.domain.resp.CommonResp;

public enum Status{
    SYSTEM_ERROR(1000,"System error"),
    REQUEST_PARAMS_ERROR(1001,"Invalid request parameters"),

    USERNAME_EXIST(4000,"The username already exists"),
    EMAIL_EXIST(4001,"The email already exists"),
    USERNAME_NOT_EXIST(4003,"The username does not exist"),
    INCORRECT_PASSWORD(4004,"Incorrect password"),
    LOGIN_EXPIRED(4005,"The login session has expired"),
    INCORRECT_CODE(4006,"Incorrect code"),
    SUCCESS(5000,"SUCCESS");

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
