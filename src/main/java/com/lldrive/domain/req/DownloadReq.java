package com.lldrive.domain.req;

import lombok.Data;

@Data
public class DownloadReq {
    private String username;
    private String dirName;

    private String fileName;
}
