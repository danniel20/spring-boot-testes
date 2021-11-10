package com.securityteste.securityspringteste.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Papel extends Base implements GrantedAuthority{
    
    private String nome;

    @ManyToMany(mappedBy = "papeis")
    private List<Usuario> usuarios;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = "ROLE_" + nome;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
