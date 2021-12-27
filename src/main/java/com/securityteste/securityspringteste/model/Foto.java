package com.securityteste.securityspringteste.model;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

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
@SequenceGenerator(name = "sequence_id", sequenceName = "foto_sequence_id", allocationSize = 1)
public class Foto extends Base {

	private static final long serialVersionUID = 1L;

	private String fileName;

	//@Column(nullable = false)
	private String path;
}
