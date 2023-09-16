package com.lldrive.service.impl;

import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.RepoMapper;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lldrive.domain.consts.Const.UUID_LENGTH;


@Service
public class UserFileServiceImpl implements UserFileService {
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    RepoMapper repoMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public CommonResp addFileToUser(File file, UploadFileReq uploadFileReq) {
        User uploadUser=userMapper.selectByUsername(uploadFileReq.getUsername());
        if(uploadUser==null){
            return new CommonResp(Status.USERNAME_NOT_EXIST);
        }
        UserFile userFile=userFileMapper.selectUserFileBytRepoIdAndFileNameAndDirID(uploadUser.getRepoId(), uploadFileReq.getFileName(),uploadFileReq.getDirId());
        if(userFile!=null){
            return new CommonResp(Status.ALREADY_HAVE_FILE);
        }
        UserFile uploadFile=new UserFile();
        uploadFile.setFileId(file.getFileId());
        uploadFile.setFileName(uploadFileReq.getFileName());
        uploadFile.setDir(uploadFileReq.isDir());
        uploadFile.setDirId(uploadFileReq.getDirId());
        uploadFile.setUserFileId(UUIDUtil.generate(UUID_LENGTH));
        uploadFile.setType(file.getType());
        uploadFile.setRepoId(uploadUser.getRepoId());
        int res=userFileMapper.insert(uploadFile);
        if(res==1){
            return new CommonResp(Status.SUCCESS,uploadFile);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
}
