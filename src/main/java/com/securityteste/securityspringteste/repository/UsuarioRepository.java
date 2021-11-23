package com.securityteste.securityspringteste.repository;

import java.util.Optional;

import com.securityteste.securityspringteste.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Optional<Usuario> findByLogin(String login);
}
