package com.securityteste.securityspringteste.api.controller.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {

    private String type;
    private String token;
    
}
