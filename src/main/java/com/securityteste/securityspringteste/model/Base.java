package com.securityteste.securityspringteste.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Base implements Serializable {

    private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id")
    protected Long id;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected LocalDate createdAt;

	@LastModifiedDate
	protected LocalDate updatedAt;

}
