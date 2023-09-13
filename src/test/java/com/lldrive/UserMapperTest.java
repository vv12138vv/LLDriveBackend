package com.lldrive;

import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.User;
import com.lldrive.mapper.UserMapper;
import com.lldrive.service.UserService;
import com.lldrive.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void createUser(){
        User user=new User();
        user.setUserId(UUIDUtil.generate(32));
        user.setRepoId(UUIDUtil.generate(32));
        user.setEmail("test@test.com");
        user.setUsername("test");
        user.setPassword("test");
        user.setBanned(false);
        user.setAdmin(false);
        userMapper.insert(user);
    }
    @Test
    public void selectUser(){
        User user=userMapper.selectByUsername("tes");
        System.out.println(user);
    }
}
