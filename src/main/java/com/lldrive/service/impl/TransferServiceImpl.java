package com.lldrive.service.impl;

import cn.hutool.db.StatementUtil;
import com.alibaba.druid.support.spring.stat.annotation.Stat;
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
import com.lldrive.service.UserFileService;
import io.lettuce.core.StrAlgoArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
//import java.lang.ref.Cleaner;

import java.lang.ref.Cleaner;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import static com.lldrive.domain.consts.Const.*;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    ChunkMapper chunkMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    UserFileService userFileService;
    @Value("C:\\file_storage")
    private String FILE_STORE_PATH;
    private static final Long DEFAULT_CHUNK_SIZE= (long) (5 * 1024 * 1024);//20MB
//    @Override
//    public CommonResp upload(UploadFileReq uploadFileReq) {
//        com.lldrive.domain.entity.File file = fileMapper.selectFileByHash(uploadFileReq.getHash());
//        if (file != null) {
//            return new CommonResp(Status.FILE_EXIST, file);
//        }
//        boolean chunkFlag = uploadFileReq.getChunkFlag();
//        if (!chunkFlag) {
//            return simpleUpload(uploadFileReq);
//        }
//        return chunkUpload(uploadFileReq);
//    }

    @Override
    public CommonResp upload(UploadFileReq uploadFileReq){
        com.lldrive.domain.entity.File file=fileMapper.selectFileByHash(uploadFileReq.getHash());
        if(file!=null){
            return new CommonResp(Status.FAST_UPLOAD_SUCCESS,file);
        }
        if(null==uploadFileReq.getFile()){
            return new CommonResp(Status.PARAM_ERROR);
        }
        File savePath=new File(FILE_STORE_PATH);
        if(!savePath.exists()){
            boolean isCreated=savePath.mkdir();
            if(!isCreated){
                return new CommonResp(Status.SYSTEM_ERROR);
            }
        }
        String extName=FileUtil.getFileExtension(uploadFileReq.getFileName());
        String fullFileName=savePath+File.separator+uploadFileReq.getHash()+'.'+extName;
        //单文件上传
        if(uploadFileReq.getTotalChunks()==1){
            boolean isUploaded=singleUpload(uploadFileReq,fullFileName);
            if(!isUploaded){
                return new CommonResp(Status.SYSTEM_ERROR);
            }
            com.lldrive.domain.entity.File fileRecord = new com.lldrive.domain.entity.File();//数据库添加信息
            String randomId=UUIDUtil.generate(FILE_NAME_LENGTH);
            String fileName=savePath+File.separator+randomId+'.'+extName;
            fileRecord.setPath(fileName);
            fileRecord.setType(extName);
            fileRecord.setHash(uploadFileReq.getHash());
            fileRecord.setFileId(randomId);
            fileRecord.setSize(uploadFileReq.getTotalSize());
            int res = fileMapper.insert(fileRecord);
            if(res!=1){
                return new CommonResp(Status.SYSTEM_ERROR);
            }
            return new CommonResp(Status.SUCCESS,fileRecord);
        }
        uploadChunk(uploadFileReq,fullFileName);
        Chunk chunk=new Chunk();
        chunk.setChunkNumber(uploadFileReq.getChunkNumber());
        chunk.setChunkSize(DEFAULT_CHUNK_SIZE);
        chunk.setTotalChunk(Long.valueOf(uploadFileReq.getTotalChunks()));
        chunk.setHash(uploadFileReq.getHash());
        chunk.setCurrentChunkSize(uploadFileReq.getFile().getSize());
        chunkMapper.insert(chunk);
        Integer count=chunkMapper.chunkCount(uploadFileReq.getHash());
        if(Objects.equals(count, uploadFileReq.getTotalChunks())){
            com.lldrive.domain.entity.File fileRecord=new com.lldrive.domain.entity.File();
            String randomId=UUIDUtil.generate(FILE_NAME_LENGTH);
            String fileName=savePath+File.separator+randomId+'.'+extName;
            File tempName=new File(fullFileName);
            File newName=new File(fileName);
            tempName.renameTo(newName);
            fileRecord.setPath(fileName);
            fileRecord.setType(extName);
            fileRecord.setHash(uploadFileReq.getHash());
            fileRecord.setFileId(randomId);
            fileRecord.setSize(uploadFileReq.getTotalSize());
            fileMapper.insert(fileRecord);
            chunkMapper.deleteByHash(uploadFileReq.getHash());
            return new CommonResp(Status.SUCCESS,fileRecord);
        }
        Map<String,String> res=new HashMap<>();
        res.put("status","uploading");
        return new CommonResp(Status.CHUNK_SUCCESS,res);
    }

    @Override
    public boolean singleUpload(UploadFileReq uploadFileReq,String fullFileName){
        File saveFile=new File(fullFileName);
        try{
            uploadFileReq.getFile().transferTo(saveFile);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean uploadChunk(UploadFileReq uploadFileReq,String fullFileName){
        try(RandomAccessFile randomAccessFile=new RandomAccessFile(fullFileName,"rw")){
            Long chunkSize=DEFAULT_CHUNK_SIZE;
            if(uploadFileReq.getChunkSize()!=null&&uploadFileReq.getChunkSize()!=0L){
                chunkSize=uploadFileReq.getChunkSize();
            }
            Long offset=chunkSize*uploadFileReq.getChunkNumber();
            randomAccessFile.seek(offset);
            randomAccessFile.write(uploadFileReq.getFile().getBytes());
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    @Override
//    public CommonResp simpleUpload(UploadFileReq uploadFileReq) {
//        MultipartFile file = uploadFileReq.getFile();
//        File baseFile = new File(FILE_STORE_PATH);
//        if (!baseFile.exists()) {
//            baseFile.mkdirs();
//        }
//        try {
//            String extension = FileUtil.getFileExtension(uploadFileReq.getFileName());//获取拓展名
//            if (extension == null) {
//                return new CommonResp(Status.INVALID_EXTENSION);
//            }
//            String newName = UUIDUtil.generate(FILE_NAME_LENGTH);
//            file.transferTo(new File(baseFile, newName + "." + extension));//存入储
//            com.lldrive.domain.entity.File fileRecord = new com.lldrive.domain.entity.File();//数据库添加信息
//            String filePath = FILE_STORE_PATH + File.separator + newName + "." + extension;
//            fileRecord.setPath(filePath);
//            fileRecord.setType(extension);
//            fileRecord.setHash(uploadFileReq.getHash());
//            fileRecord.setFileId(newName);
//            fileRecord.setSize(uploadFileReq.getFile().getSize());
//            int res = fileMapper.insert(fileRecord);
//            if (res == 1) {
//                return new CommonResp(Status.SUCCESS, fileRecord);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new CommonResp(Status.SYSTEM_ERROR);
//    }

    @Override
    public CommonResp fastUpload(UploadFileReq uploadFileReq) {
        com.lldrive.domain.entity.File file = fileMapper.selectFileByHash(uploadFileReq.getHash());
        if (file != null) {
            userFileService.addFileToUser(file,uploadFileReq);
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


//    public CommonResp chunkUpload(UploadFileReq uploadFileReq){
//        Boolean lastFlag=false;
//        String fileName=uploadFileReq.getFileName();
////        String parentDir=FILE_STORE_PATH+File.separator+uploadFileReq.getHash()+File.separator;
//        String tempFilename=fileName+"_tmp";
//        //写入临时文件
//        try{
//            File tmpFile=tmpFile(FILE_STORE_PATH,tempFilename,uploadFileReq.getFile(),uploadFileReq.getChunkNumber(),uploadFileReq.getTotalSize(),uploadFileReq.getHash());
//            Integer chunkCount= chunkMapper.chunkCount(uploadFileReq.getHash());
//            if(Objects.equals(chunkCount, uploadFileReq.getTotalChunks())){
//                lastFlag=true;
//            }
//            if(lastFlag){//若已为最后一片
////                if(!checkHash(tmpFile,uploadFileReq.getHash())){//若hash不匹配
////                    cleanUp(tmpFile,uploadFileReq.getHash());
////                    return new CommonResp(Status.HASH_ERROR);
////                }
////                if(!renameFile(tmpFile,uploadFileReq.getFileName())) {
////                    return new CommonResp(Status.SYSTEM_ERROR);
////                }
//                addNewFile(tmpFile,uploadFileReq.getHash(),uploadFileReq.getTotalSize());
//                return new CommonResp(Status.SUCCESS);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new CommonResp(Status.CHUNK_SUCCESS);
//    }
//
//    private File tmpFile(String dir,String tmpFileName,MultipartFile file,Integer chunkNumber,Integer totalSize,String fileHash)throws IOException{
//        Long position=((chunkNumber)*CHUNK_SIZE);
//        File tmpDir=new File(dir);
//        File tmpFile=new File(dir,tmpFileName);
//        if(!tmpDir.exists()){
//            tmpDir.mkdirs();
//        }
//        RandomAccessFile raf=new RandomAccessFile(tmpFile,"rw");//读写权限
//        if(raf.length()==0){
//            raf.setLength(totalSize);
//        }
//        //写入分片数据
//        FileChannel fc=raf.getChannel();
////        MappedByteBuffer map=fc.map(FileChannel.MapMode.READ_WRITE,position,file.getSize());//将文件的一部分映射到内存中方便读写
////        map.put(file.getBytes());//写入
////        clean(map);
//        ByteBuffer buffer= ByteBuffer.allocate((int) file.getSize());
//        buffer.put(file.getBytes());
//        buffer.flip();
//        fc.position(position);
//        fc.write(buffer);
//        fc.close();
//        raf.close();
//        //记录已完成的分片
//        Chunk chunk=new Chunk();
//        chunk.setChunkNumber(chunkNumber);
//        chunk.setHash(fileHash);
//        chunk.setCurrentChunkSize(file.getSize());
//        int res=chunkMapper.insert(chunk);
//        if(res==1){
//            return tmpFile;
//        }
//        return null;
//    }

    private Boolean checkHash(File file,String hash) throws IOException{//检查MD5值
        FileInputStream fis=new FileInputStream(file);
        String fileHash= Arrays.toString(DigestUtils.md5Digest(fis));
        fis.close();
        if(fileHash.equals(hash)){
            return true;
        }
        return false;
    }

//    private void cleanUp(File file,String hash){//删除已有分片记录
//        if(file.exists()){
//            file.delete();
//        }
//        int res=chunkMapper.deleteByHash(hash);
//    }
//
//    private boolean renameFile(File toBeRenamed, String newName) {
//        // 检查要重命名的文件是否存在，是否是文件
//        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
//            return false;
//        }
//        String parentPath = toBeRenamed.getParent();
//        File newFile = new File(parentPath + File.separatorChar + newName);
//        // 如果存在, 先删除
//        if (newFile.exists()) {
//            newFile.delete();
//        }
//        boolean res=toBeRenamed.renameTo(newFile);
//        return res;
//    }
//
//
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
//    private static void clean(MappedByteBuffer map) {
//        try {
//            Method getCleanerMethod = map.getClass().getMethod("cleaner");
//            Cleaner.create(map, null);
//            getCleanerMethod.setAccessible(true);
//            Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(map);
//            cleaner.clean();
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//private static void clean(MappedByteBuffer map) {
//    try {
//        Method getCleanerMethod = map.getClass().getMethod("cleaner");
//        getCleanerMethod.setAccessible(true);
//        Object cleanerObj = getCleanerMethod.invoke(map);
//
//        Method cleanMethod = cleanerObj.getClass().getMethod("clean");
//        cleanMethod.invoke(cleanerObj);
//    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//}
//    private static void clean(MappedByteBuffer map){
//        try{
//            Method cleanMethond=map.getClass().getMethod("cleaner");
//            cleanMethond.setAccessible(true);
//            Cleaner cleaner=(Cleaner) cleanMethond.invoke(map);
//            cleaner.clean();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }




}
