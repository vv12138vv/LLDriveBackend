package com.lldrive.domain.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
@Data
public class RegisterReq {
    @Length(min = 4,max=24,message = "The username length should be between 6-24")
    private String username;
    @Email
    private String email;
    @NotBlank
    @Length(min=6,max=24,message = "The password length should be between 6-24")
    private String password;
    @NotBlank
    private String code;
}
