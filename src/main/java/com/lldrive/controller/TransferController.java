package com.lldrive.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class TransferController {

    @PostMapping("/api/transfers/upload")
    public void up(String nickname, MultipartFile file, HttpServletRequest req) throws IOException{
        System.out.println(nickname);
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());
    }
}
