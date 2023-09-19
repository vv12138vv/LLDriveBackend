package com.lldrive.service.impl;

import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.Repo;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.File;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.MoveFileReq;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.ChunkMapper;
import com.lldrive.mapper.RepoMapper;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
//        uploadFile.setDir(uploadFileReq.isDir());
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

    @Override
    public CommonResp<List<UserFile>> listUserFiles(User user,String dirId){
        List<UserFile> userFiles=userFileMapper.selectUserFilesByRepoIdAndDirId(user.getRepoId(),dirId);
        return new CommonResp<List<UserFile>>(Status.SUCCESS,userFiles);
    }

    @Override
    public CommonResp createDir(User user, String dirId, String dirName) {
        UserFile dir=userFileMapper.selectUserFileBytRepoIdAndFileNameAndDirID(user.getRepoId(),dirName,dirId);
        if(dir!=null){
            return new CommonResp(Status.ALREADY_HAVE_FILE);
        }
        UserFile newDir=new UserFile();
        newDir.setUserFileId(UUIDUtil.generate(UUID_LENGTH));
        newDir.setIsDir(true);
        newDir.setDirId(dirId);
        newDir.setFileName(dirName);
        newDir.setRepoId(user.getRepoId());
        int res=userFileMapper.insert(newDir);
        if(res==1){
            return new CommonResp(Status.SUCCESS,newDir);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
    @Override
    public CommonResp deleteUserFile(User user,String userFileId){
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(userFileId);
        if(userFile==null){
            return new CommonResp(Status.FILE_NOT_EXIST);
        }
        if(!userFile.getIsDir()){//若不是文件夹
            int res=userFileMapper.updateUserFileDeleted(userFileId,true);
            if(res==1){
                return new CommonResp(Status.SUCCESS);
            }
            return new CommonResp(Status.SYSTEM_ERROR);
        }
        List<UserFile> deleteDirs=new LinkedList<UserFile>();//记录文件夹
        Queue<UserFile> queue=new LinkedList<UserFile>();//辅助队列
        deleteDirs.add(userFile);
        queue.add(userFile);
        while(!queue.isEmpty()){
            UserFile temp=queue.poll();
            List<UserFile> dirs=userFileMapper.selectDirsByDirId(temp.getUserFileId());
            for(UserFile dir:dirs){
                queue.add(dir);
                deleteDirs.add(dir);
            }
        }
        for(UserFile dir:deleteDirs){
            if(dir.getDirId()!=null){
                userFileMapper.updateUserFilesDeleted(dir.getUserFileId(),user.getRepoId(),true);
            }
        }
        userFileMapper.updateUserFileDeleted(userFileId,true);
        return new CommonResp(Status.SUCCESS);
    }

    @Override
    public CommonResp<List<UserFile>> searchUserFiles(User user, String fileName) {
        List<UserFile> searchResult=userFileMapper.selectUserFilesByRepoIdAndFilename(user.getRepoId(),fileName);
        if(searchResult.size()==0){
            return new CommonResp (Status.FILE_NOT_EXIST);
        }
        return new CommonResp<List<UserFile>>(Status.SUCCESS,searchResult);
    }

    @Override
    public CommonResp findUserFile(String userFileId) {
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(userFileId);
        if(userFile==null){
            return new CommonResp(Status.FILE_NOT_EXIST);
        }
        return new CommonResp(Status.SUCCESS,userFile);
    }

    @Override
    public CommonResp renameUserFile(String userFileId, String newName) {
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(userFileId);
        if(userFile==null){
            return new CommonResp(Status.FILE_NOT_EXIST);
        }
        List<UserFile> userFiles=userFileMapper.selectUserFilesByDirId(userFile.getDirId());
        for(UserFile userfile:userFiles){
            if(newName.equals(userfile.getFileName())){
                return new CommonResp(Status.FILE_NAME_EXIST);
            }
        }
        int res=userFileMapper.updateUserFileName(userFileId,newName);
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp moveUserFile(MoveFileReq moveFileReq) {
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(moveFileReq.getUserFileId());
        if(userFile==null){
            return new CommonResp(Status.FILE_NAME_EXIST);
        }
        List<UserFile> userFiles=userFileMapper.selectUserFilesByDirId(moveFileReq.getDirId());
        for(UserFile file:userFiles){
            if(file.getFileName().equals(userFile.getFileName())){
                return new CommonResp(Status.FILE_NAME_EXIST);
            }
        }
        int res=userFileMapper.updateUserFileDir(moveFileReq.getUserFileId(),moveFileReq.getDirId());
        if(res==1){
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
    @Override
    public CommonResp<List<UserFile>> listDeletedUserFiles(User user){
        List<UserFile> deletedFiles=userFileMapper.selectDeletedFiles(user.getRepoId());
        return new CommonResp<List<UserFile>>(Status.SUCCESS,deletedFiles);
    }


    @Override
    public CommonResp recoverUserFile(User user, String userFileId) {
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(userFileId);
        if(!userFile.getIsDeleted()){
            return new CommonResp(Status.FILE_NOT_DELETE);
        }
        if(!userFile.getIsDir()){
            int res=userFileMapper.updateUserFileDeleted(userFileId,false);
            if(res==1){
                return new CommonResp(Status.SUCCESS);
            }
            return new CommonResp(Status.SYSTEM_ERROR);
        }
        List<UserFile> recoverDirs=new LinkedList<UserFile>();//记录文件夹
        Queue<UserFile> queue=new LinkedList<UserFile>();//辅助队列
        recoverDirs.add(userFile);
        queue.add(userFile);
        while(!queue.isEmpty()){
            UserFile temp=queue.poll();
            List<UserFile> dirs=userFileMapper.selectDirsByDirId(temp.getUserFileId());
            for(UserFile dir:dirs){
                queue.add(dir);
                recoverDirs.add(dir);
            }
        }
        for(UserFile dir:recoverDirs){
            if(dir.getDirId()!=null){
                userFileMapper.updateUserFilesDeleted(dir.getUserFileId(),user.getRepoId(),false);
            }
        }
        userFileMapper.updateUserFileDeleted(userFileId,false);
        return new CommonResp(Status.SUCCESS);


    }
}
