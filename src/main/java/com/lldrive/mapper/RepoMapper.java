package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.Repo;
import org.apache.ibatis.annotations.Select;

public interface RepoMapper extends BaseMapper<Repo> {
    @Select("select * from repos and users where users.username=#{username}")
    Repo selectRepoByUsername(String username);
}
