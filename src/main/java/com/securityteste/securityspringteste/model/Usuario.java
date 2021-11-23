package com.securityteste.securityspringteste.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@Entity
public class Usuario extends Base implements UserDetails{

    @NotBlank
    @Size(min=3, message = "deve possuir ao menos 3 caracteres.")
    @Column(unique=true)
    private String login;

    @NotBlank
    @Size(min=6, message = "deve possuir ao menos 6 caracteres.")
    private String senha;

    @NotBlank
    private String nome;
    
    @Email(message = "Email inválido")
    @Column(unique=true)
    private String email;

    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usuarios_papeis", 
	            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "login"), 
	            inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "nome")) 
    private List<Papel> papeis;

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
        return this.papeis;
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
