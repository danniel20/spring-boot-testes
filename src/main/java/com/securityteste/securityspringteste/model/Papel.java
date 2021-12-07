package com.securityteste.securityspringteste.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuarios"})
@Entity
@SequenceGenerator(name = "sequence_id", sequenceName = "papel_sequence_id", allocationSize = 1)
public class Papel extends Base implements GrantedAuthority{

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, unique=true)
    private String nome;

    @ManyToMany(mappedBy = "papeis")
    private List<Usuario> usuarios;

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
