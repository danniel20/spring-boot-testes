package com.securityteste.securityspringteste.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Past;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "sequence_id", sequenceName = "usuario_sequence_id", allocationSize = 1)
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

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH}*/)
	@JoinTable(name = "usuarios_papeis", 
	            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), 
	            inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Papel> papeis = new HashSet<Papel>();

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
