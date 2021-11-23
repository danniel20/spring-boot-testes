package com.securityteste.securityspringteste.controller.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginDTO {
    
    @NotBlank
    @Size(min=3, message = "deve possuir ao menos 3 caracteres.")
    private String login;

    @NotBlank
    @Size(min=6, message = "deve possuir ao menos 6 caracteres.")
    private String password;
}
