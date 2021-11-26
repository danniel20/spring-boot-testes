package com.securityteste.securityspringteste.service.usuarios;

import java.util.List;
import java.util.Optional;

import com.securityteste.securityspringteste.model.Usuario;

public interface UsuarioService {

    public Usuario salvar(Usuario usuario);
    public Optional<Usuario> bucarPorId(Long id);
    public List<Usuario> buscarTodos();
    public void deletarPorId(Long id);
    public Optional<Usuario> buscarPorLogin(String login);
}
