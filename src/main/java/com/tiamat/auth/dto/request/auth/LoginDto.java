package com.tiamat.auth.dto.request.auth;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}
