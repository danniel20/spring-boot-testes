package com.securityteste.securityspringteste.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import javax.validation.constraints.Past;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Usuario extends Base implements UserDetails{

    @Column(nullable = false, unique=true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable=false, unique=true)
    private String email;

    @Past
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usuarios_papeis", 
	            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "login"), 
	            inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "nome")) 
    private Set<Papel> papeis;

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
