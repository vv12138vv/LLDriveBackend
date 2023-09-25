package com.lldrive.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordReq {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
