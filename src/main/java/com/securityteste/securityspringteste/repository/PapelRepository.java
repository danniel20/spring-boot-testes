package com.securityteste.securityspringteste.repository;

import com.securityteste.securityspringteste.model.Papel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Long>{
    
}
