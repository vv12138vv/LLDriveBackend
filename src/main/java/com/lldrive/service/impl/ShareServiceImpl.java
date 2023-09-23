package com.lldrive.service.impl;

import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.SharedFile;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.ShareFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.ShareMapper;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.ShareService;
import com.lldrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.lldrive.domain.consts.Const.UUID_LENGTH;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    ShareMapper shareMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    UserMapper userMapper;
    public CommonResp findShareRecord(String sharedId){
       SharedFile sharedFile= shareMapper.selectBySharedId(sharedId);
       if(sharedFile==null){
           return new CommonResp(Status.SHARE_NOT_EXIST);
       }
       return new CommonResp(Status.SUCCESS,sharedFile);
    }

    @Override
    public CommonResp shareFile(ShareFileReq shareFileReq) {
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(shareFileReq.getUserFileId());
        if(userFile==null){
            return new CommonResp(Status.FILE_NOT_EXIST);
        }
        SharedFile sharedFile=new SharedFile();
        sharedFile.setSharedId(UUIDUtil.generate(UUID_LENGTH));
        sharedFile.setUserFileId(userFile.getUserFileId());
        sharedFile.setCode(shareFileReq.getCode());
        sharedFile.setShareTime(Timestamp.valueOf(LocalDateTime.now()));//以下用于计算expireTime
        LocalDateTime localDateTime=sharedFile.getShareTime().toLocalDateTime();
        LocalDateTime expireTime=localDateTime.plusMinutes(shareFileReq.getExpireTime());
        sharedFile.setExpireTime(Timestamp.valueOf(expireTime));
        int res=shareMapper.insert(sharedFile);
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp updateSharedCount(String sharedId){
        int res=shareMapper.updateSharedCount(sharedId);
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
    @Override
    public CommonResp listShareRecord(){
        List<SharedFile> sharedFiles=shareMapper.selectSharedFiles();
        return new CommonResp(Status.SUCCESS,sharedFiles);
    }



}