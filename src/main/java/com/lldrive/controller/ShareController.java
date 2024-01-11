package com.lldrive.controller;

import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.SharedFile;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.SavaFileReq;
import com.lldrive.domain.req.ShareFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.service.FileService;
import com.lldrive.service.ShareService;
import com.lldrive.service.UserFileService;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
public class ShareController {
    @Autowired
    ShareService shareService;
    @Autowired
    UserService userService;
    @Autowired
    UserFileService userFileService;
    @PostMapping("/save")
    CommonResp saveFile(@Validated @RequestBody SavaFileReq saveFileReq){
        CommonResp shareResp=shareService.findShareRecord(saveFileReq.getSharedId());
        if(shareResp.getData()==null){
            return shareResp;
        }
        SharedFile sharedFile=(SharedFile) shareResp.getData();
        if(sharedFile.getValidType()!=0){
            if(!sharedFile.getCode().equals(saveFileReq.getCode())){
                return new CommonResp(Status.INCORRECT_CODE);
            }
        }
        CommonResp userResp=userService.findUser(saveFileReq.getUsername());
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User) userResp.getData();

        CommonResp userFileResp=userFileService.findUserFile(sharedFile.getUserFileId());
        if(userFileResp.getData()==null){
            return userResp;
        }
        UserFile userFile=(UserFile)userFileResp.getData();
        shareService.updateSharedCount(sharedFile.getSharedId());
        shareService.cleanExpireShare();
        return userFileService.addFileToUser(userFile,user,saveFileReq.getDirId());
    }
    @PostMapping("")
    CommonResp shareFile(@Validated @RequestBody ShareFileReq shareFileReq){
        shareService.cleanExpireShare();
        return shareService.shareFile(shareFileReq);
    }

    @GetMapping("/list")
    CommonResp listSharedFile(@RequestParam("page_no")String reqPageNo,@RequestParam("page_size")String reqPageSize){
        Integer pageNo=Integer.parseInt(reqPageNo);
        Integer pageSize=Integer.parseInt(reqPageSize);
        shareService.cleanExpireShare();
        return shareService.listShareRecord(pageNo,pageSize);
    }

    @GetMapping("/visitors/list")
    CommonResp listVisitorFiles(@RequestParam("page_no")String reqPageNo,@RequestParam("page_size")String reqPageSize){
        Integer pageNo=Integer.parseInt(reqPageNo);
        Integer pageSize=Integer.parseInt(reqPageSize);
        shareService.cleanExpireShare();
        return shareService.listVisitorFiles(pageNo,pageSize);
    }

    @GetMapping("/clean")
    CommonResp cleanExpireShare(){
        return  shareService.cleanExpireShare();
    }
}
