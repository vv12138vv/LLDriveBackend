package com.lldrive.service;

import com.lldrive.domain.req.ShareFileReq;
import com.lldrive.domain.resp.CommonResp;

public interface ShareService {
    CommonResp findShareRecord(String sharedId);

    CommonResp shareFile(ShareFileReq shareFileReq);

    CommonResp updateSharedCount(String sharedId);

    CommonResp listShareRecord(Integer pageNo,Integer pageSize);

    CommonResp cleanExpireShare();

    CommonResp listVisitorFiles(Integer pageNo,Integer pageSize);
}
