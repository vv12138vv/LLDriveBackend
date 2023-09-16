package com.lldrive.controller;

import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.service.TransferService;
import com.lldrive.service.UserFileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController()
@RequestMapping("/api/transfers")
public class TransferController {
    @Autowired
    TransferService transferService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserFileService userFileService;
    @PostMapping("/upload")
    public CommonResp upload(UploadFileReq uploadFileReq) {
        CommonResp resp = transferService.upload(uploadFileReq);
        if (resp.getData() == null) {
            return resp;
        }
        File file = (File) resp.getData();
        CommonResp addResp=userFileService.addFileToUser(file,uploadFileReq);
        if(addResp.getData()==null){
            return addResp;
        }
        return new CommonResp(Status.SUCCESS);
    }

    @GetMapping("/upload")
    public CommonResp fastUpload(UploadFileReq uploadFileReq){
        return transferService.fastUpload(uploadFileReq);
    }

}
