package com.lldrive.service.impl;

import com.lldrive.Utils.FileUtil;
import com.lldrive.Utils.UUIDUtil;
import com.lldrive.domain.entity.Chunk;
import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.ChunkMapper;
import com.lldrive.mapper.FileMapper;
import com.lldrive.mapper.UserFileMapper;
import com.lldrive.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Objects;

import static com.lldrive.domain.consts.Const.*;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    ChunkMapper chunkMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Value("C:\\Users\\jjjjssky\\Desktop\\LLDrive\\LLDrive\\src\\main\\resources\\file_storage")
    private String FILE_STORE_PATH;

    @Override
    public CommonResp upload(UploadFileReq uploadFileReq) {
        com.lldrive.domain.entity.File file = fileMapper.selectFileByHash(uploadFileReq.getHash());
        if (file != null) {
            return new CommonResp(Status.FILE_EXIST, file);
        }
        boolean chunkFlag = uploadFileReq.getChunkFlag();
        if (!chunkFlag) {
            return simpleUpload(uploadFileReq);
        }
        return chunkUpload(uploadFileReq);
    }

    @Override
    public CommonResp simpleUpload(UploadFileReq uploadFileReq) {
        MultipartFile file = uploadFileReq.getFile();
        File baseFile = new File(FILE_STORE_PATH);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        try {
            String extension = FileUtil.getFileExtension(uploadFileReq.getFileName());//获取拓展名
            if (extension == null) {
                return new CommonResp(Status.INVALID_EXTENSION);
            }
            String newName = UUIDUtil.generate(FILE_NAME_LENGTH);
            file.transferTo(new File(baseFile, newName + "." + extension));//存入储
            com.lldrive.domain.entity.File fileRecord = new com.lldrive.domain.entity.File();//数据库添加信息
            String filePath = FILE_STORE_PATH + File.separator + newName + "." + extension;
            fileRecord.setPath(filePath);
            fileRecord.setType(extension);
            fileRecord.setHash(uploadFileReq.getHash());
            fileRecord.setFileId(newName);
            fileRecord.setSize(uploadFileReq.getFile().getSize());
            int res = fileMapper.insert(fileRecord);
            if (res == 1) {
                return new CommonResp(Status.SUCCESS, fileRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }

    @Override
    public CommonResp fastUpload(UploadFileReq uploadFileReq) {
        com.lldrive.domain.entity.File file = fileMapper.selectFileByHash(uploadFileReq.getHash());
        if (file != null) {
            return new CommonResp(Status.SUCCESS, true);//快速上传成功
        } else {
            return new CommonResp(Status.SUCCESS, false);//不能快速上传
        }
    }

    @Override
    public CommonResp findFile(UserFile userFile) {
        com.lldrive.domain.entity.File file=fileMapper.selectFileByFileId(userFile.getFileId());
        if(file==null){
            return new CommonResp(Status.FILE_NOT_EXIST);
        }
        return new CommonResp(Status.SUCCESS,file);
    }

    public CommonResp chunkUpload(UploadFileReq uploadFileReq){
        Boolean lastFlag=false;
        String tmpFileName=uploadFileReq.getFileName()+"_tmp";
        //写入临时文件
        try{
            File tmpFile=tmpFile(FILE_STORE_PATH,tmpFileName,uploadFileReq.getFile(),uploadFileReq.getChunkNumber(),uploadFileReq.getTotalSize(),uploadFileReq.getHash());
            Integer chunkCount= chunkMapper.chunkCount(uploadFileReq.getHash());
            if(Objects.equals(chunkCount, uploadFileReq.getTotalChunks())){
                lastFlag=true;
            }
            if(lastFlag){//若已为最后一片
                if(!checkHash(tmpFile,uploadFileReq.getHash())){//若hash不匹配
                    cleanUp(tmpFile,uploadFileReq.getHash());
                    return new CommonResp(Status.HASH_ERROR);
                }
                if(!renameFile(tmpFile,uploadFileReq.getFileName())) {
                    return new CommonResp(Status.SYSTEM_ERROR);
                }
                addNewFile(tmpFile,uploadFileReq.getHash(),uploadFileReq.getTotalSize());
                return new CommonResp(Status.SUCCESS);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private File tmpFile(String dir,String tmpFileName,MultipartFile file,Integer chunkNumber,Integer totalSize,String fileHash)throws IOException{
        Long position=((chunkNumber-1)*CHUNK_SIZE);
        File tmpDir=new File(dir);
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }
        File tmpFile=new File(tmpDir,tmpFileName);
        RandomAccessFile raf=new RandomAccessFile(tmpFile,"rw");//读写权限
        if(raf.length()==0){
            raf.setLength(totalSize);
        }
        //写入分片数据
        FileChannel fc=raf.getChannel();
        MappedByteBuffer map=fc.map(FileChannel.MapMode.READ_WRITE,position,file.getSize());//将文件的一部分映射到内存中方便读写
        map.put(file.getBytes());//写入
        fc.close();
        raf.close();
        //记录已完成的分片
        Chunk chunk=new Chunk();
        chunk.setChunkNumber(chunkNumber);
        chunk.setHash(fileHash);
        chunk.setIsUpload(true);
        chunk.setCurrentChunkSize(file.getSize());
        int res=chunkMapper.insert(chunk);
        if(res==1){
            return tmpFile;
        }
        return null;
    }

    private Boolean checkHash(File file,String hash) throws IOException{//检查MD5值
        FileInputStream fis=new FileInputStream(file);
        String fileHash= Arrays.toString(DigestUtils.md5Digest(fis));
        fis.close();
        if(fileHash.equals(hash)){
            return true;
        }
        return false;
    }

    private void cleanUp(File file,String hash){//删除已有分片记录
        if(file.exists()){
            file.delete();
        }
        int res=chunkMapper.deleteByHash(hash);
    }

    private boolean renameFile(File toBeRenamed, String newName) {
        // 检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            return false;
        }
        String parentPath = toBeRenamed.getParent();
        File newFile = new File(parentPath + File.separatorChar + newName);
        // 如果存在, 先删除
        if (newFile.exists()) {
            newFile.delete();
        }
        return toBeRenamed.renameTo(newFile);
    }


    public boolean addNewFile(File file,String hash,Integer totalSize){//数据库中添加文件记录
        com.lldrive.domain.entity.File fileRecord=new com.lldrive.domain.entity.File();
        fileRecord.setFileId(UUIDUtil.generate(UUID_LENGTH));
        fileRecord.setPath(file.getPath());
        fileRecord.setType(FileUtil.getFileExtension(file.getName()));
        fileRecord.setHash(hash);
        fileRecord.setSize(Long.valueOf(totalSize));
        int res=fileMapper.insert(fileRecord);
        if(res==1){
            return true;
        }
        return false;
    }
}
