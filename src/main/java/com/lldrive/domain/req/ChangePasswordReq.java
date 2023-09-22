package com.lldrive.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
