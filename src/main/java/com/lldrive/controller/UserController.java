package com.lldrive.controller;

import com.lldrive.domain.entity.User;
import com.lldrive.domain.req.LoginReq;
import com.lldrive.domain.req.ResetPasswordReq;
import com.lldrive.domain.req.SetNewPasswordReq;
import com.lldrive.domain.req.ChangePasswordReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.req.RegisterReq;
import com.lldrive.domain.types.Status;
import com.lldrive.service.EmailService;
import com.lldrive.service.RepoService;
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
    @Autowired
    private RepoService repoService;
    @GetMapping("/register")
    public CommonResp sendRegisterCode(@NotNull @Email String email){
        return userService.sendRegisterCode(email);
    }
    @PostMapping("/register")
    public CommonResp registerUser(@Validated @RequestBody RegisterReq registerReq){
        CommonResp registerResp=userService.register(registerReq);
        if(registerResp.getData()==null){
            return registerResp;
        }
        User user=(User) registerResp.getData();
        CommonResp repoResp=repoService.createRepo(user.getUserId(),user.getRepoId());
        if(repoResp.getData()==null){
            return repoResp;
        }
        return new CommonResp(Status.SUCCESS);
    }

    @PostMapping("/login")
    public  CommonResp loginUser(@Validated @RequestBody LoginReq loginReq){
        return userService.login(loginReq);
    }

    @GetMapping("/info")
    public CommonResp userInfo(@RequestHeader("X-Token")String token){
        return userService.getUserInfo(token);
    }

    @PostMapping("/logout")
    public CommonResp userLogOut(@RequestHeader("X-Token")String token){
        return userService.logout(token);
    }


    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Validated @RequestBody ResetPasswordReq resetPasswordReq){
        return userService.resetPassword(resetPasswordReq);
    }
    @GetMapping("/reset-password")//发送认证邮件
    public CommonResp sendResetCode(@Validated @Email @RequestParam("email")String email){
        return userService.sendResetCode(email);
    }

    @PostMapping("/set-new-password")
    public CommonResp setNewPassword(@Validated @RequestBody SetNewPasswordReq setNewPasswordReq){
        return userService.setNewPasword(setNewPasswordReq);
    }

    @PostMapping("/change-password")
    public CommonResp changePassword(@Validated @RequestBody ChangePasswordReq changePasswordReq){
        CommonResp userResp=userService.changePassword(changePasswordReq);
        if(userResp.getData()==null){
            return userResp;
        }
        return new CommonResp(Status.SUCCESS);
    }

    @GetMapping("/list")
    public CommonResp userAllList(@RequestParam("page_no")String reqPageNo,@RequestParam("page_size")String reqPageSize){
        Integer pageNo=Integer.parseInt(reqPageNo);
        Integer pageSize=Integer.parseInt(reqPageSize);
        return userService.listAllUser(pageNo,pageSize);
    }

    @GetMapping("/change-status")
    public CommonResp banUser(@RequestParam("user_id")String userId,@RequestParam("is_banned")String reqIsBanned){
        Integer banned=Integer.parseInt(reqIsBanned);
        Boolean isBanned= banned == 1;
        return userService.changeUserStatus(userId,isBanned);
    }
    @GetMapping("/change-capacity")
    public CommonResp changeCapacity(@RequestParam("user_id")String userId,@RequestParam("new_capacity")String reqNewCapacity){
        Long newCapacity=Long.parseLong(reqNewCapacity)*1024*1024;
        return userService.changeCapacity(userId,newCapacity);
    }
}
