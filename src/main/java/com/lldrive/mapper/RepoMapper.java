package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.Repo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface RepoMapper extends BaseMapper<Repo> {
    @Select("select * from repos and users where users.username=#{username}")
    Repo selectRepoByUsername(String username);

    @Update("update repos set cur_capacity=cur_capacity+#{fileSize} where repo_id=#{repoId}")
    int updateCurCapacity(@Param("repoId")String repoId,@Param("fileSize")Long fileSize);
}
