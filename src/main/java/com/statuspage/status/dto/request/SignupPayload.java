package com.statuspage.status.dto.request;


import com.statuspage.status.model.Role;
import lombok.Data;

@Data
public class SignupPayload {

    private String name;

    private String email;

    private Role role;

    private String password;


}
