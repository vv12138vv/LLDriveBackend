package com.lldrive.service;

import com.lldrive.domain.entity.User;
import com.lldrive.domain.entity.File;
import com.lldrive.domain.req.UploadFileReq;
import com.lldrive.domain.resp.CommonResp;


public interface UserFileService {
    CommonResp addFileToUser(File file, UploadFileReq uploadFileReq);
}
