package com.lldrive.controller;

import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.service.TransferService;
import com.lldrive.service.UserFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
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
        if (!Objects.equals(resp.getStatusCode(), Status.SUCCESS.getCode())) {
            return resp;
        }
        File file = (File) resp.getData();
        CommonResp addResp=userFileService.addFileToUser(file,uploadFileReq);
        if(addResp.getData()==null){
            return addResp;
        }
        Map<String,String> res=new HashMap<>();
        res.put("status","upload_finish");
        return new CommonResp(Status.SUCCESS,res);
    }

    @GetMapping("/upload")
    public CommonResp fastUpload(UploadFileReq uploadFileReq){
        return transferService.fastUpload(uploadFileReq);
    }

    @GetMapping("/download")
    public CommonResp download(@RequestParam("user_file_id")String userFileId,HttpServletResponse resp){
        CommonResp userFileResp=userFileService.findUserFile(userFileId);
        if(userFileResp.getData()==null){
            return userFileResp;
        }
        UserFile userFile=(UserFile)userFileResp.getData();
        CommonResp fileResp=transferService.findFile(userFile);
        if(fileResp.getData()==null){
            return fileResp;
        }
        File file=(File)fileResp.getData();
        try{
            FileInputStream in=new FileInputStream(file.getPath());
//            resp.setHeader("Content-Disposition","attachment;filename="+userFile.getFileName());
            resp.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(userFile.getFileName(),"UTF-8"));
            resp.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            resp.setHeader("Content-Type", "application/octet-stream");
            BufferedOutputStream out=new BufferedOutputStream(resp.getOutputStream());
            byte []b=new byte[1024];
            int len;
            while((len=in.read(b))!=-1){
                out.write(b,0,len);
            }
            in.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResp(Status.SYSTEM_ERROR);
        }
        return new CommonResp(Status.SUCCESS);
    }

}
