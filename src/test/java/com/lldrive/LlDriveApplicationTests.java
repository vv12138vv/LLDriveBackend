package com.lldrive;

import com.lldrive.Utils.UUIDUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

@SpringBootTest
class LlDriveApplicationTests {

    @Resource
    JavaMailSender sender;
    @Test
    void contextLoads() {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setSubject("[蓝鲤网盘]注册账号验证码");
        mailMessage.setText("wdnmd");
        mailMessage.setTo("987611676@qq.com");
        mailMessage.setFrom("LLDrive@163.com");
        sender.send(mailMessage);
    }
    @Test
    void testUUIDGenerate(){
        System.out.println(UUID.randomUUID());
    }

}
