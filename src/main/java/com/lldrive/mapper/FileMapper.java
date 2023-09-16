package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.File;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FileMapper extends BaseMapper<File> {

    @Select("select * from files where hash=#{hash}")
    File selectFileByHash(@Param("hash") String hash);
}
