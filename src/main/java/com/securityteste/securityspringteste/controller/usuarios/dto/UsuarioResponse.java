package com.securityteste.securityspringteste.controller.usuarios.dto;

import lombok.Data;

@Data
public class UsuarioResponse {
    
    private Long id;
    private String login;
    private String nome;
    private String email;
}
