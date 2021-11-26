package com.securityteste.securityspringteste.controller.usuarios.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRequest{
    
    @NotBlank
    @Size(min=3, message = "deve possuir ao menos 3 caracteres.")
    private String login;

    @NotBlank
    @Size(min=6, message = "deve possuir ao menos 6 caracteres.")
    private String senha;

    @NotBlank
    private String nome;

    @NotBlank
    @Email(message = "Email inválido")
    private String email;

    @NotBlank
    @Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9])", message = "Informe uma data válida!. Ex: dd/MM/yyyy")
    private String dataNascimento;
    
    private String[] papeis;
}
