package com.lldrive.config;

import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)//全局参数异常处理
    public CommonResp<String> argumentNotValidHandler(MethodArgumentNotValidException e){
        ObjectError objectError=e.getBindingResult().getAllErrors().get(0);
        return new CommonResp<String>(Status.REQUEST_PARAMS_ERROR,objectError.getDefaultMessage());
    }

}
