package com.securityteste.securityspringteste.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import lombok.Getter;
import lombok.NoArgsConstructor;
// import lombok.Setter;

//@Getter
//@Setter
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "seq_foto", sequenceName = "foto_id_seq", allocationSize = 1)
public class Foto extends Base {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_foto")
	@EqualsAndHashCode.Include
    private Long id;

	private String fileName;

	private String path;

	@OneToOne
	@JoinColumn(nullable = false)
	@NotNull
	private Usuario usuario;

	@Builder.Default
	@Embedded
	private Timestamps timestamps = new Timestamps();
}
