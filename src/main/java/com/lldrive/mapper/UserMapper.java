package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from users where username=#{username}")
    User selectByUsername(@Param("username")String username);

    @Select("select * from users where email=#{email}")
    User selectByEmail(@Param("email")String email);
}
