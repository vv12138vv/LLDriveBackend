package com.lldrive.mapper;

import com.lldrive.domain.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from users")
    public List<User> findAll();
}
