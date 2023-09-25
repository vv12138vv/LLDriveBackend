package com.lldrive.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
 public class SetNewPasswordReq {
    @NotBlank
    private String email;
    @NotBlank
    @JsonProperty("new_password")
    private String newPassword;
}
