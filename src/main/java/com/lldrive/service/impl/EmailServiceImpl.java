package com.lldrive.service.impl;

import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender sender;
    @Override
    public CommonResp sendEmail(String to, String subject,String content){
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(content, true);
            sender.send(message);
            return new CommonResp(Status.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
}
