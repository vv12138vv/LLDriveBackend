package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.UserFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserFileMapper extends BaseMapper<UserFile> {
    @Select("select * from user_files where repo_id=#{repoId} and file_id=#{fileId}")
    UserFile selectUserFileByRepoIdAndFileId(@Param("repoId")String repoId, @Param("fileId")String fileId);

    @Select("select * from user_files where repo_id=#{repoId} and file_name=#{fileName} and  dir_id=#{dirId}")
    UserFile selectUserFileBytRepoIdAndFileNameAndDirID(@Param("repoId")String repoId,@Param("fileName")String fileName,@Param("dirId")String dirId);

    @Select("select * from user_files where repo_id=#{repoId} and dir_id=#{dirId}")
    List<UserFile>  selectUserFilesByRepoIdAndDirId(@Param("repoId")String repoId,@Param("dirId")String dirId);

    @Select("select * from user_files where user_file_id=#{userFileId}")
    UserFile selectUserFileByUserFileId(@Param("userFileId")String userFileId);

    @Select("select * from user_files where dir_id=#{dirId}")
    List<UserFile> selectUserFilesByDirId(@Param("dirId")String dirId);

    @Update("update user_files set is_deleted=#{deleted} where user_file_id=#{userFileId}")
    int updateUserFileDeleted(@Param("userFileId")String userFileId,@Param("deleted")boolean deleted);

    @Update("update user_files set is_deleted=#{deleted} where dir_id=#{dirId} and repo_id=#{repoId}")
    int updateUserFilesDeleted(@Param("dirId")String dirId,@Param("repoId")String repoId,@Param("deleted")boolean deleted);

    @Select("select * from user_files where dir_id=#{dirId} and is_dir=1")
    List<UserFile> selectDirsByDirId(@Param("dirId")String dirId);

    @Select("select * from user_files where repo_id=#{repoId} and file_name like concat('%',#{fileName},'%')")
    List<UserFile> selectUserFilesByRepoIdAndFilename(@Param("repoId")String repoId,@Param("fileName")String fileName);

    @Update("update user_files set file_name=#{newName} where user_file_id=#{userFileId}")
    int updateUserFileName(@Param("userFileId")String userFileId,@Param("newName")String newName);
}
