package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.Chunk;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChunkMapper extends BaseMapper<Chunk> {

    @Select("select * from chunks where chunk_id=#{chunkId} and chunk_size=#{chunkSize}")
    List<Chunk> selectAllByChunkIdAndChunkSize(@Param("chunk_id")String chunkId,@Param("chunk_size") Double chunkSize);
}
