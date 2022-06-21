package com.securityteste.securityspringteste.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
//import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.Setter;
import lombok.ToString;

@Builder
//@Getter
//@Setter
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = { "usuarios" })
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "seq_papel", sequenceName = "papel_id_seq", allocationSize = 1)
public class Papel extends Base implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_papel")
	@EqualsAndHashCode.Include
    private Long id;

	@Column(nullable = false, unique = true)
	private String nome;

	@ManyToMany(mappedBy = "papeis")
	private List<Usuario> usuarios;

	@Override
	public String getAuthority() {
		return this.nome;
	}
}
