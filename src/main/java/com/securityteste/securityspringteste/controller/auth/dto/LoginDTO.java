package com.securityteste.securityspringteste.controller.auth.dto;



import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginDTO {
    
    @NotEmpty
    private String login;

    @NotEmpty
    @Min(value = 6)
    private String password;
}
