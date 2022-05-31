package com.securityteste.securityspringteste.model;

import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@Data
@MappedSuperclass
public abstract class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id")
    protected Long id;

}
