package com.lldrive.controller;

import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.RegisterResp;
import com.lldrive.domain.types.Status;
import com.lldrive.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public CommonResp<Map<String,String>> sendRegisterCode(@Email String email){
        userService.sendEmailCode(email);
        return null;
    }
    @PostMapping("/register")
    public RegisterResp registerUser(@Validated @RequestBody RegisterReq registerReq){

        return new RegisterResp();
    }

    @PostMapping("/login")
    public  CommonResp loginUser(@Validated @RequestBody LoginReq loginReq){
        return userService.login(loginReq);
    }
}
