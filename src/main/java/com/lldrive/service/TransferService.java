package com.lldrive.service;

import com.lldrive.domain.entity.UserFile;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;

public interface TransferService {

    CommonResp upload(UploadFileReq uploadFileReq);


    CommonResp fastUpload(UploadFileReq uploadFileReq);

    CommonResp findFile(UserFile userFile);

    boolean singleUpload(UploadFileReq uploadFileReq,String fullFileName);
    boolean uploadChunk(UploadFileReq uploadFileReq,String fullFileName);
}
