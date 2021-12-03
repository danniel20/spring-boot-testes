package com.securityteste.securityspringteste.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuarios"})
@Entity
@SequenceGenerator(name = "sequence_id", sequenceName = "papel_sequence_id", allocationSize = 1)
public class Papel extends Base implements GrantedAuthority{
    
    @Getter
    @NotBlank
    private String nome;

    @Getter @Setter
    @ManyToMany(mappedBy = "papeis"/*, cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH}*/)
    private List<Usuario> usuarios;

    public void setNome(String nome) {
        this.nome = "ROLE_" + nome;
    }

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
