package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.UserFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserFileMapper extends BaseMapper<UserFile> {
    @Select("select * from user_files where repo_id=#{repoId} and file_id=#{fileId}")
    UserFile selectUserFileByRepoIdAndFileId(@Param("repoId")String repoId, @Param("fileId")String fileId);

    @Select("select * from user_files where repo_id=#{repoId} and file_name=#{fileName} and  dir_id=#{dirId}")
    UserFile selectUserFileBytRepoIdAndFileNameAndDirID(@Param("repoId")String repoId,@Param("fileName")String fileName,@Param("dirId")String dirId);
}
