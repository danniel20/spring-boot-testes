package com.securityteste.securityspringteste.service.auth;

import java.util.Optional;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = this.usuarioRepository.findByLogin(username);

        if(usuario.isPresent()){
            return usuario.get();
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado!");        
    }
    
}
