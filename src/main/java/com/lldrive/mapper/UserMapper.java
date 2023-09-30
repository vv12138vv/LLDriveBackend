package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.UserFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from users where username=#{username}")
    User selectByUsername(@Param("username")String username);

    @Select("select * from users where email=#{email}")
    User selectByEmail(@Param("email")String email);

    @Select("select * from users where user_id=#{userId}")
    User selectByUserId(@Param("userId")String userId);

    @Select("select * from users where repo_id=#{repoId}")
    User selectByRepoId(@Param("repoId")String repoId);

    @Update("update users set password=#{password} where email=#{email}")
    int updatePassword(@Param("email")String email,@Param("password")String password);
    @Select("select COUNT(*) from users where is_admin=0")
    Integer countUser();

    @Select("select * from users where is_admin=0 order by id ASC limit #{pageSize} offset #{offset}")
    List<User> selectUsersByPage(@Param("pageSize")Integer pageSize,@Param("offset")Integer offset);

    @Update("update users set is_banned=#{isBanned} where user_id=#{userId}")
    Integer updateUserBanned(@Param("userId")String userId,@Param("isBanned")Boolean isBanned);
}
