package com.securityteste.securityspringteste.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario extends Base implements UserDetails{

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usuarios_papeis", 
	            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "login"), 
	            inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "nome")) 
    private List<Papel> papeis;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return papeis;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}