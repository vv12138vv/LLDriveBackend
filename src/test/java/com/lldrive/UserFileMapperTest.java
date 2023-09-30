package com.lldrive;

import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.service.UserFileService;
import com.lldrive.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserFileMapperTest {
//    @Autowired
//    UserFileMapper userFileMapper;
//
//    @Autowired
//    UserFileService userFileService;
//
//    @Autowired
//    UserService userService;
//    @Test
//    public void testSearch(){
//        String repoId="80b3a2230fa6474cb49a88971eedb0f9";
//        List<UserFile> searchResult=userFileMapper.selectUserFilesByRepoIdAndFilename(repoId,"Folder");
//        for(UserFile userFile:searchResult){
//            System.out.println(userFile);
//        }
//    }
//
//    @Test
//    public void testDeleteAndRecycle(){
//        String repoId="80b3a2230fa6474cb49a88971eedb0f9";
//        CommonResp  userResp =userService.findUser("test");
//        User user=(User) userResp.getData();
//        userFileService.deleteUserFile(user,"a3722b37b64a42b9bb290bf5ad94c2f0");
//        List<UserFile> deletedFiles=userFileMapper.selectDeletedFiles(repoId);
//    }
}
