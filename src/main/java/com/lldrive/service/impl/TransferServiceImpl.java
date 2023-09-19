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
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import static com.lldrive.domain.consts.Const.FILE_NAME_LENGTH;

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
            return singleUpload(uploadFileReq);
        }
        return chunkUpload(uploadFileReq);
    }

    @Override
    public CommonResp chunkUpload(UploadFileReq uploadFileReq) {
        return null;
    }

    @Override
    public CommonResp singleUpload(UploadFileReq uploadFileReq) {
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
            file.transferTo(new File(baseFile, newName + "." + extension));//存入储存池
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
}
