package com.linasdeli.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangedRequestDTO {
    private String currentPassword;
    private String newPassword;
}
