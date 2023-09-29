package com.lldrive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lldrive.domain.entity.UserFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserFileMapper extends BaseMapper<UserFile> {
    @Select("select * from user_files where repo_id=#{repoId} and file_id=#{fileId}")
    UserFile selectUserFileByRepoIdAndFileId(@Param("repoId")String repoId, @Param("fileId")String fileId);

    @Select("select * from user_files where repo_id=#{repoId} and file_name=#{fileName} and  dir_id=#{dirId} and is_deleted=0")
    UserFile selectUserFileBytRepoIdAndFileNameAndDirID(@Param("repoId")String repoId,@Param("fileName")String fileName,@Param("dirId")String dirId);

    @Select("select * from user_files where repo_id=#{repoId} and dir_id=#{dirId} and is_deleted=0")
    List<UserFile>  selectUserFilesByRepoIdAndDirId(@Param("repoId")String repoId,@Param("dirId")String dirId);
    @Select("select COUNT(*) from user_files where repo_id=#{repoId} and is_deleted=0 and dir_id=#{dirId}")
    Integer countUserFilesByRepoIdAndDirId(@Param("repoId")String repoId,@Param("dirId")String dirId);
    @Select("select COUNT(*) from user_files where repo_id=#{repoId} and is_deleted=1")
    Integer countRecycleByRepo(@Param("repoId")String repoId);
    @Select("select * from user_files where repo_id=#{repoId} and dir_id=#{dirId} and is_deleted=0 order by id ASC limit #{pageSize} offset #{offset}")
    List<UserFile> selectUserFilesByRepoIdAndDirIdPage(@Param("repoId")String repoId,@Param("dirId")String dirId,@Param("pageSize")Integer pageSize,@Param("offset")Integer offset);
    @Select("select * from user_files where user_file_id=#{userFileId}")
    UserFile selectUserFileByUserFileId(@Param("userFileId")String userFileId);

    @Select("select * from user_files where dir_id=#{dirId} and is_deleted=0")
    List<UserFile> selectUserFilesByDirId(@Param("dirId")String dirId);

    @Update("update user_files set is_deleted=#{deleted} where user_file_id=#{userFileId}")
    int updateUserFileDeleted(@Param("userFileId")String userFileId,@Param("deleted")boolean deleted);

    @Update("update user_files set is_deleted=#{deleted} where dir_id=#{dirId} and repo_id=#{repoId}")
    int updateUserFilesDeleted(@Param("dirId")String dirId,@Param("repoId")String repoId,@Param("deleted")boolean deleted);

    @Delete("delete from user_files where repo_id=#{repoId} and dir_id=#{dirId} and is_deleted=1")
    int deleteUserFilesInRecycle(@Param("repoId")String repoId,@Param("dirId")String dirId);

    @Select("select * from user_files where dir_id=#{dirId} and is_dir=1")
    List<UserFile> selectDirsByDirId(@Param("dirId")String dirId);

    @Select("select * from user_files where repo_id=#{repoId} and is_deleted=0 and (file_name like concat('%',#{fileName},'%')) order by id ASC limit #{pageSize} offset #{offset}")
    List<UserFile> selectUserFilesByRepoIdAndFilename(@Param("repoId")String repoId,@Param("fileName")String fileName,@Param("pageSize")Integer pageSize,@Param("offset")Integer offset);
    @Select("select COUNT(*) from user_files where repo_id=#{repoId} and is_deleted=0 and (file_name like concat('%',#{fileName},'%'))")
    Integer countUserFilesByRepoIdAndFilename(@Param("repoId")String repoId,@Param("fileName")String fileName);

    @Update("update user_files set file_name=#{newName} where user_file_id=#{userFileId} and is_deleted=0")
    int updateUserFileName(@Param("userFileId")String userFileId,@Param("newName")String newName);

    @Update("update user_files set dir_id=#{dirId} where user_file_id=#{userFileId}")
    int updateUserFileDir(@Param("userFileId")String userFileId,@Param("dirId")String dirId);


    @Select("select * from user_files where repo_id=#{repoId} and is_deleted=1 and (dir_id='' or dir_id not in (select user_file_id from user_files where is_deleted=1 and repo_id=#{repoId})) order by id ASC limit #{pageSize} offset #{offset} ")
    List<UserFile> selectDeletedFiles(@Param("repoId")String repoId,@Param("pageSize")Integer pageSize,@Param("offset")Integer offset);

    @Update("update user_files set size=size+#{fileSize} where user_file_id=#{userFileId}")
    int updateUserFileSize(@Param("userFileId")String userFileId,@Param("fileSize")Long fileSize);

}
