package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.Chunk;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChunkMapper extends BaseMapper<Chunk> {

    @Select("select * from chunks where hash=#{hash}")
    List<Chunk> selectChunksByHash(@Param("hash")String hash);

    @Select("select COUNT(*) from chunks where hash=#{hash}")
    Integer chunkCount(@Param("hash")String hash);

    @Delete("delete from chunks where hash=#{hash}")
    int deleteByHash(@Param("hash")String hash);
}
