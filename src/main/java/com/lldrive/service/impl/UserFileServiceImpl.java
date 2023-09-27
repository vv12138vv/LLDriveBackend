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

import java.util.*;

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
        uploadFile.setIsDir(uploadFileReq.getIsDir());
        if(uploadFileReq.getDirId()==null){
            uploadFile.setDirId(new String(""));
        }else{
            uploadFile.setDirId(uploadFileReq.getDirId());
        }
        uploadFile.setUserFileId(UUIDUtil.generate(UUID_LENGTH));
        uploadFile.setType(file.getType());
        uploadFile.setRepoId(uploadUser.getRepoId());
        uploadFile.setSize(file.getSize());
        int res=userFileMapper.insert(uploadFile);
        if(res==1){
            this.updateDirSize(uploadFile.getUserFileId(),true);
            repoMapper.updateCurCapacity(uploadUser.getRepoId(), uploadFile.getSize());
            return new CommonResp(Status.SUCCESS,uploadFile);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp addFileToUser(UserFile userFile,User user,String dirId){
        UserFile newUserFile=new UserFile();
        newUserFile.setFileName(userFile.getFileName());
        newUserFile.setUserFileId(UUIDUtil.generate(UUID_LENGTH));
        newUserFile.setType(userFile.getType());
        newUserFile.setDirId(dirId);
        newUserFile.setFileId(userFile.getFileId());
        newUserFile.setRepoId(user.getRepoId());
        newUserFile.setIsDir(userFile.getIsDir());
        int res=userFileMapper.insert(newUserFile);
        if(res==1){
            this.updateDirSize(userFile.getUserFileId(),true);//更新父文件大小
            repoMapper.updateCurCapacity(user.getRepoId(), userFile.getSize());//更新仓库大小
            return new CommonResp(Status.SUCCESS);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp<List<UserFile>> listUserFiles(User user,String dirId){
        List<UserFile> userFiles=userFileMapper.selectUserFilesByRepoIdAndDirId(user.getRepoId(),dirId);
        return new CommonResp<List<UserFile>>(Status.SUCCESS,userFiles);
    }
    @Override
    public CommonResp listUserFilesByPage(User user,String dirId,Integer pageNo,Integer pageSize){
        Integer count= userFileMapper.countUserFilesByRepoIdAndDirId(user.getRepoId(),dirId);
        Integer pageTotal=count/pageSize+1;
        Integer offset=(pageNo-1)*pageSize;
        List<UserFile> userFiles=userFileMapper.selectUserFilesByRepoIdAndDirIdPage(user.getRepoId(),dirId,pageSize,offset);
        Integer totalCount=userFiles.size();
        Map<String, Object> result=new HashMap<>();
        result.put("total_count",totalCount);
        result.put("page_size",pageSize);
        result.put("page_no",pageNo);
        result.put("page_total",pageTotal);
        result.put("list",userFiles);
        return new CommonResp<>(Status.SUCCESS,result);
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
                repoMapper.updateCurCapacity(user.getRepoId(), -userFile.getSize());
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
                userFileMapper.updateUserFilesDeleted(dir.getUserFileId(),user.getRepoId(),true);//使该目录下的所有文件逻辑删除
            }
        }
        userFileMapper.updateUserFileDeleted(userFileId,true);
        this.updateDirSize(userFileId,false);
        repoMapper.updateCurCapacity(user.getRepoId(), -userFile.getSize());
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
        List<UserFile> userFiles=userFileMapper.selectUserFilesByDirId(moveFileReq.getDirId());//重名检查
        for(UserFile file:userFiles){
            if(file.getFileName().equals(userFile.getFileName())){
                return new CommonResp(Status.FILE_NAME_EXIST);
            }
        }
        this.updateDirSize(userFile.getUserFileId(),false);//减少当前父文件夹大小
        int res=userFileMapper.updateUserFileDir(moveFileReq.getUserFileId(),moveFileReq.getDirId());//更新父亲文件夹
        if(res==1){
            this.updateDirSize(moveFileReq.getUserFileId(),true);//增加当前父文件夹大小
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
                this.updateDirSize(userFileId,true);
                repoMapper.updateCurCapacity(user.getRepoId(), userFile.getSize());
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
        this.updateDirSize(userFileId,true);
        repoMapper.updateCurCapacity(user.getRepoId(),userFile.getSize());
        return new CommonResp(Status.SUCCESS);
    }

    //flag==true为+，false为-
    public void updateDirSize(String userFileId,boolean flag){//迭代更新父文件夹大小
        UserFile userFile=userFileMapper.selectUserFileByUserFileId(userFileId);
        if(userFile==null){
            return;
        }
        Queue<UserFile>queue=new LinkedList<UserFile>();
        UserFile dir=userFileMapper.selectUserFileByUserFileId(userFile.getDirId());
        if(dir==null){
            return;
        }
        queue.add(dir);
        while(!queue.isEmpty()){
            UserFile temp=queue.poll();
            if(flag){
                userFileMapper.updateUserFileSize(temp.getUserFileId(), userFile.getSize());
            }else{
                userFileMapper.updateUserFileSize(temp.getUserFileId(), -userFile.getSize());
            }
            UserFile father=userFileMapper.selectUserFileByUserFileId(temp.getDirId());
            if(father!=null){
                queue.add(father);
            }
        }
    }
}
