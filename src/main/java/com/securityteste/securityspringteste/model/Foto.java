package com.securityteste.securityspringteste.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "sequence_id", sequenceName = "foto_id_seq", allocationSize = 1)
public class Foto extends Base {

	private static final long serialVersionUID = 1L;

	private String fileName;

	private String path;

	@OneToOne
	@JoinColumn(nullable = false)
	@NotNull
	private Usuario usuario;
}
