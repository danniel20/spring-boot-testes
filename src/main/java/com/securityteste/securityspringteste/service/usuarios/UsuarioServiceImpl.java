package com.securityteste.securityspringteste.service.usuarios;

import java.util.List;
import java.util.Optional;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

	@Transactional
    @Override
    public Usuario salvar(Usuario usuario) {
        return this.repository.save(usuario);

    }

    @Override
    public Optional<Usuario> bucarPorId(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return this.repository.findAll();
    }

    @Override
	@Transactional
    public void deletarPorId(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return this.repository.findByLogin(login);
    }

}
