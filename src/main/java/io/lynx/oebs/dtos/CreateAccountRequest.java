package io.lynx.oebs.dtos;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String lang;
}
