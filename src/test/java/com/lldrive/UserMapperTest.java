package com.lldrive;

import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.User;
import com.lldrive.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

//    @Autowired
//    private UserMapper userMapper;
//    @Test
//    public void createUser(){
//        User user=new User();
//        user.setUserId(UUIDUtil.generate(32));
//        user.setRepoId(UUIDUtil.generate(32));
//        user.setEmail("test@test.com");
//        user.setUsername("test");
//        user.setPassword("test");
//        user.setIsBanned(false);
//        user.setIsAdmin(false);
//        userMapper.insert(user);
//    }
//    @Test
//    public void selectUser(){
//        User user=userMapper.selectByUsername("tes");
//        User user1=userMapper.selectByEmail("test@test.com");
//        if(user1==null){
//            System.out.println(true);
//        }
//        System.out.println(user);
//    }

}
