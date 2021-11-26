package com.securityteste.securityspringteste.config.database;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

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

        Set<Papel> papeis = new HashSet<Papel>();
        papeis.add(roleUser);

        Usuario usuario1 = new Usuario();
        usuario1.setLogin("ana");
        usuario1.setSenha(new BCryptPasswordEncoder().encode("123456"));
        usuario1.setNome("Ana Maria");
        usuario1.setEmail("usuario1@teste.com");
        usuario1.setDataNascimento(LocalDate.parse("25/09/1989", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        usuario1.setPapeis(papeis);

        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setLogin("joao");
        usuario2.setSenha(new BCryptPasswordEncoder().encode("123456"));
        usuario2.setNome("Joao das Neves");
        usuario2.setEmail("usuario2@teste.com");
        usuario2.setDataNascimento(LocalDate.parse("18/02/1982", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        papeis.add(roleAdmin);
        usuario2.setPapeis(papeis);

        usuarioRepository.save(usuario2);        
    }
    
}
