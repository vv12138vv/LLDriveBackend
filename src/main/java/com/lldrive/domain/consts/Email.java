package com.lldrive.domain.consts;

public class Email {
    static class Subject{
        public static final String RESET_PASSWORD="密码重置";
        public static final String USER_REGISTER="用户注册";
    }
    static class Content{
        public static final String USER_REGISTER="亲爱的用户，您好!<p>这是一封用户注册邮件，验证码有效时间为%d分钟。</p> <p><b>%s</b></p><p>如果并非您本人操作，可以忽略此邮件</p>";
    }
}
