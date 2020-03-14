package com.statuspage.status.dto.request;

import lombok.Data;

@Data
public class SigninPayload {

    private String name;

    private String email;

    private String password;
}
