package com.securityteste.securityspringteste.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "sequence_id", sequenceName = "usuario_id_seq", allocationSize = 1)
public class Usuario extends Base implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Size(min = 3, message = "deve possuir ao menos 3 caracteres.")
	@Column(nullable = false, unique = true)
	private String login;

	@Size(min = 6, message = "deve possuir ao menos 6 caracteres.")
	@Column(nullable = false)
	private String senha;

	@NotBlank
	@Column(nullable = false)
	private String nome;

	@NotBlank
	@Email(message = "Informe um email válido.")
	@Column(nullable = false, unique = true)
	private String email;

	@NotNull(message = "Informe uma data.")
	@Past
	@Column(nullable = false)
	private LocalDate dataNascimento;

	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
	private Foto foto;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_papeis", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "id"))
	@Builder.Default
	private Set<Papel> papeis = new HashSet<Papel>();

	@Builder.Default
	@Embedded
	private Timestamps timestamps = new Timestamps();

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
