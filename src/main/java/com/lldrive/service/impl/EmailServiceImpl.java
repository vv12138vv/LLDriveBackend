package com.lldrive.service.impl;

import com.lldrive.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private String from;
    @Autowired
    private JavaMailSender sender;
    @Override
    public void sendSimpleEmail(String to, String content) {

    }
}
