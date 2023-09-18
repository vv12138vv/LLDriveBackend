package com.lldrive.controller;

import com.lldrive.domain.req.MkDirReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.service.UserFileService;
import com.lldrive.service.UserService;
import com.lldrive.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    UserFileService userFileService;
    @Autowired
    UserService userService;


    @GetMapping("/list")
    CommonResp fileList(@RequestParam("username")String username,@RequestParam("dir_id")String dirId){
        CommonResp userResp=userService.findUser(username);
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User) userResp.getData();
        CommonResp resp=userFileService.listUserFile(user,dirId);
        return resp;
    }

    @PostMapping("/mkdir")
    CommonResp createDir(@Validated @RequestBody MkDirReq mkDirReq){
        CommonResp userResp=userService.findUser(mkDirReq.getUsername());
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User)userResp.getData();
        CommonResp resp=userFileService.createDir(user, mkDirReq.getDirId(), mkDirReq.getDirName());
        if(resp.getData()==null){
            return resp;
        }
        return new CommonResp(Status.SUCCESS);
    }

    @GetMapping("/delete")
    CommonResp deleteFile(@RequestParam("user_file_id")String userFileId,@RequestParam("username")String username){
        CommonResp userResp=userService.findUser(username);
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User)userResp.getData();
        CommonResp resp=userFileService.deleteUserFile(user,userFileId);
        return resp;
    }
}
