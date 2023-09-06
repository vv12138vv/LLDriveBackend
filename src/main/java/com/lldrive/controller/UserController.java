package com.lldrive.controller;

import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.resp.RegisterResp;
import com.lldrive.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public CommonResp sendRegisterCode(@Email String email){
        System.out.println(email);
        return new CommonResp();
    }
    @PostMapping("/register")
    public RegisterResp registerUser(@Validated @RequestBody RegisterReq registerReq){
        return new RegisterResp();
    }
}
