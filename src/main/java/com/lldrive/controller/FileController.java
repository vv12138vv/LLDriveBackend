package com.lldrive.controller;

import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.ListRecycleReq;
import com.lldrive.domain.req.MkDirReq;
import com.lldrive.domain.req.MoveFileReq;
import com.lldrive.domain.req.UserFileListReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.service.UserFileService;
import com.lldrive.service.UserService;
import com.lldrive.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    UserFileService userFileService;
    @Autowired
    UserService userService;


    @GetMapping("/list")
    CommonResp fileList(@RequestParam("username")String username,@RequestParam("dir_id")String dirId,@RequestParam("type")String type){
        CommonResp userResp=userService.findUser(username);
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User) userResp.getData();
        CommonResp<List<UserFile>> resp=userFileService.listUserFiles(user,dirId);
        List<UserFile> userFiles=resp.getData();
        if (type==null||type.equals("")){//若无类型要求，则返回所有类型文件
            return resp;
        }
        for(UserFile userFile:userFiles){
            if(type.equals("folder")){//如类型为文件夹
                if(!userFile.getIsDir()){
                    userFiles.remove(userFile);
                }
            }else{
                if(!userFile.getType().equals(type)){
                    userFiles.remove(userFile);
                }
            }
        }
        return resp;
    }

    @PostMapping("/list")
    CommonResp listUserFiles(@Validated @RequestBody UserFileListReq userFileListReq){
        if(userFileListReq.getDirId()==null){
            userFileListReq.setDirId("");
        }
        Integer pageNo=Integer.parseInt(userFileListReq.getPageNo());
        Integer pageSize=Integer.parseInt(userFileListReq.getPageSize());
        CommonResp userResp=userService.findUser(userFileListReq.getUsername());
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User) userResp.getData();
        if(userFileListReq.getFileName()==null||userFileListReq.equals("")){
            return userFileService.listUserFilesByPage(user, userFileListReq.getDirId(),pageNo,pageSize);
        }else{
            return userFileService.listSearchUserFileByPage(user,userFileListReq.getFileName(),pageNo,pageSize);
        }
    }

    @PostMapping("/mkdir")
    CommonResp createDir(@Validated @RequestBody MkDirReq mkDirReq){
        if(mkDirReq.getDirId()==null){
            mkDirReq.setDirId("");
        }
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
        return userFileService.deleteUserFile(user,userFileId);
    }

//    @GetMapping("/search")
//    CommonResp searchFile(@RequestParam("username")String username,@RequestParam("file_name")String fileName){//支持模糊搜索
//        CommonResp userResp=userService.findUser(username);
//        if(userResp.getData()==null){
//            return userResp;
//        }
//        User user=(User)userResp.getData();
//        CommonResp<List<UserFile>> searchResult=userFileService.searchUserFiles(user,fileName);
//        return searchResult;
//    }

    @GetMapping("/rename")
    CommonResp renameFile(@RequestParam("user_file_id")String userFileId,@RequestParam("new_name")String newName){
        return userFileService.renameUserFile(userFileId,newName);
    }

    @PostMapping("/move")
    CommonResp moveFile(@Validated @RequestBody MoveFileReq moveFileReq){
        return userFileService.moveUserFile(moveFileReq);
    }

    @PostMapping("/list/recycle")
    CommonResp listRecycle(@RequestBody ListRecycleReq listRecycleReq){
        Integer pageNo=Integer.parseInt(listRecycleReq.getPageNo());
        Integer pageSize=Integer.parseInt(listRecycleReq.getPageSize());
        CommonResp userResp=userService.findUser(listRecycleReq.getUsername());
        if(userResp.getData()==null) {
            return userResp;
        }
        User user=(User)userResp.getData();
        return userFileService.listRecycleByPage(user,pageNo,pageSize);
    }


    @GetMapping("/recover")
    CommonResp recoverFile(@RequestParam("user_file_id")String userFileId,@RequestParam("username")String username){
        CommonResp userResp=userService.findUser(username);
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User)userResp.getData();
        return userFileService.recoverUserFile(user,userFileId);
    }

    @GetMapping("/recycle/delete")
    CommonResp truelyDetele(@RequestParam("username")String username,@RequestParam("user_file_id")String userFileId){
        CommonResp userResp=userService.findUser(username);
        if(userResp.getData()==null){
            return userResp;
        }
        User user=(User)userResp.getData();
        return userFileService.truelyDeleteUserFile(user,userFileId);

    }

    @GetMapping("/dir")
    CommonResp dirName(@RequestParam("user_file_id")String userFileId){
        CommonResp userFileResp=userFileService.findUserFile(userFileId);
        if(userFileResp.getData()==null){
            return userFileResp;
        }
        UserFile userFile=(UserFile) userFileResp.getData();
        Map<String,Object> res=new HashMap<>();
        res.put("file_name",userFile.getFileName());
        return new CommonResp(Status.SUCCESS,res);
    }


}
