package com.lldrive.service;

import com.lldrive.domain.resp.CommonResp;

public interface EmailService {

    CommonResp sendEmail(String to, String subject, String content);
}
