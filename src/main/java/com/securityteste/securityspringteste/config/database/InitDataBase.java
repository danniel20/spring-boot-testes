package com.securityteste.securityspringteste.config.database;

import java.util.ArrayList;
import java.util.List;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.PapelRepository;
import com.securityteste.securityspringteste.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("development")
public class InitDataBase implements CommandLineRunner{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public void run(String... args) throws Exception {
        Papel roleUser = new Papel();
        roleUser.setNome("USER");
        papelRepository.save(roleUser);

        Papel roleAdmin = new Papel();
        roleAdmin.setNome("ADMIN");
        papelRepository.save(roleAdmin);

        List<Papel> papeis = new ArrayList<Papel>();
        papeis.add(roleUser);

        Usuario usuario1 = new Usuario();
        usuario1.setLogin("ana");
        usuario1.setSenha(new BCryptPasswordEncoder().encode("123"));
        usuario1.setPapeis(papeis);
        usuario1.setEmail("usuario1@teste.com");

        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setLogin("joao");
        usuario2.setSenha(new BCryptPasswordEncoder().encode("123"));
        papeis.add(roleAdmin);
        usuario2.setPapeis(papeis);
        usuario2.setEmail("usuario2@teste.com");

        usuarioRepository.save(usuario2);        
    }
    
}
