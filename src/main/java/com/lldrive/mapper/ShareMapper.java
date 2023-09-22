package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.SharedFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShareMapper extends BaseMapper<SharedFile> {

    @Select("select * from shared_files where shared_id=#{sharedId}")
    SharedFile selectBySharedId(@Param("sharedId")String sharedId);

    @Update("update shared_files set shared_count=shared_count+1 where shared_id=#{sharedId}")
    int updateSharedCount(@Param("sharedId")String sharedId);

    @Select("select * from shared_files")
    List<SharedFile> selectSharedFiles();
}
