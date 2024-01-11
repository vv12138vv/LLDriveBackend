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

}
