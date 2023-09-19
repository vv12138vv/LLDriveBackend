package com.lldrive;

import com.lldrive.domain.entity.UserFile;
import com.lldrive.mapper.UserFileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserFileMapperTest {
    @Autowired
    UserFileMapper userFileMapper;


    @Test
    public void testSearch(){
        String repoId="80b3a2230fa6474cb49a88971eedb0f9";
        List<UserFile> searchResult=userFileMapper.selectUserFilesByRepoIdAndFilename(repoId,"Folder");
        for(UserFile userFile:searchResult){
            System.out.println(userFile);
        }
    }
}
