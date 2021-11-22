package com.securityteste.securityspringteste.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Papel extends Base implements GrantedAuthority{
    
    @Getter
    @NotBlank
    private String nome;

    @Getter @Setter
    @ManyToMany(mappedBy = "papeis")
    private List<Usuario> usuarios;

    public void setNome(String nome) {
        this.nome = "ROLE_" + nome;
    }

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
