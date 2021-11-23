package com.securityteste.securityspringteste.service.impl;

import java.util.List;
import java.util.Optional;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.UsuarioRepository;
import com.securityteste.securityspringteste.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
        
    }

    @Override
    public Optional<Usuario> bucarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
    
}
