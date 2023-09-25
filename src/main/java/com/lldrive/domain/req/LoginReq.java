package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
    @NotBlank(message = "email must be not blank")
    private String email;
    @NotBlank(message = "password must be not blank")
    private String password;
}
