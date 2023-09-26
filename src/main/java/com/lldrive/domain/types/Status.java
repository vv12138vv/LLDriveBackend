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
    INVALID_EXTENSION(4007,"Invalid extension"),
    ALREADY_HAVE_FILE(4008,"The user alread have the file"),
    FILE_EXIST(4009,"Consider use fastUpload"),
    FILE_NOT_EXIST(4010,"The file does not exist"),
    FILE_NAME_EXIST(4011,"The file name already exist"),
    EMAIL_NOT_EXIST(4012,"The email does not exist"),
    FILE_NOT_DELETE(4013,"The file does not be deleted"),
    HASH_ERROR(4014,"Hash Code error"),
    SHARE_NOT_EXIST(4015,"The share does not exist"),
    CHUNK_SUCCESS(4016,"The chunk upload success"),
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
