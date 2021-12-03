package com.securityteste.securityspringteste.config.database;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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
        
        Papel roleAdmin = new Papel();
        roleAdmin.setNome("ADMIN");     

        this.papelRepository.saveAll(Arrays.asList(roleUser, roleAdmin));

        Usuario usuario1 = Usuario.builder()
                            .login("ana")
                            .senha(new BCryptPasswordEncoder().encode("123456"))
                            .nome("Ana Maria")
                            .email("ana@teste.com")
                            .dataNascimento(LocalDate.parse("25/09/1989", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .build();
        
        usuario1.getPapeis().add(roleUser);
                            
        Usuario usuario2 = Usuario.builder()
                            .login("joao")
                            .senha(new BCryptPasswordEncoder().encode("123456"))
                            .nome("Joao das Neves")
                            .email("joao@teste.com")
                            .dataNascimento(LocalDate.parse("18/02/1982", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .build();
        
        usuario2.getPapeis().add(roleUser);
        usuario2.getPapeis().add(roleAdmin);
        
        this.usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2));
    }
    
}
