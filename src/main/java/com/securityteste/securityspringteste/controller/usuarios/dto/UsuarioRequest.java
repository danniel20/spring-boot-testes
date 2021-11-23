package com.securityteste.securityspringteste.controller.usuarios.dto;

import lombok.Data;

@Data
public class UsuarioRequest {
    
    private String login;
    private String senha;
    private String nome;
    private String email;
    private String[] papeis;
}
