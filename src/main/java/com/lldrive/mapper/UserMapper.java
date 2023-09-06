package com.lldrive.mapper;

import com.lldrive.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from users")
    public List<User> findAll();
}
