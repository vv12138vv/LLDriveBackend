package com.lldrive.service;

import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.MoveFileReq;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;

import java.util.List;


public interface UserFileService {
    CommonResp addFileToUser(File file, UploadFileReq uploadFileReq);
    CommonResp addFileToUser(UserFile userFile,User user,String dirId);
    CommonResp<List<UserFile>> listUserFiles(User user, String dirId);

    CommonResp listUserFilesByPage(User user,String dirId,Integer pageNo,Integer pageSize);
    CommonResp<List<UserFile>> searchUserFiles(User user,String fileName);
    CommonResp createDir(User user,String dirId,String dirName);
    CommonResp deleteUserFile(User user,String userFileId);
    CommonResp findUserFile(String userFileId);
    CommonResp renameUserFile(String userFileId,String newName);
    CommonResp moveUserFile(MoveFileReq moveFileReq);
    CommonResp<List<UserFile>> listDeletedUserFiles(User user);
    CommonResp recoverUserFile(User user,String userFileId);
}
