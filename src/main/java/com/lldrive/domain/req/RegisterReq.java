package com.lldrive.domain.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
@Data
public class RegisterReq {

    @Length(min = 4,max=24,message = "用户名长度应当为4-24位")
    private String username;
    @Email
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @Length(min=6,max=24,message = "密码长度应当为6-24位")
    private String password;
    private String token;
}
