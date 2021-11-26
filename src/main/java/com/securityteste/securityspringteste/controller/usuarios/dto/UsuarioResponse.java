package com.securityteste.securityspringteste.controller.usuarios.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UsuarioResponse {
    
    private Long id;

    private String login;
   
    private String nome;
    
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
}
