package com.securityteste.securityspringteste.config.database;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.PapelRepository;
import com.securityteste.securityspringteste.repository.UsuarioRepository;
import com.securityteste.securityspringteste.service.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
//@Profile("development")
public class InitDataBase implements CommandLineRunner{

	@Value("${spring.profiles.active}")
	private String environment;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

	@Autowired
	private StorageService storageService;

    @Override
    public void run(String... args) throws Exception {

		if(this.environment.equals("development")){

			if(this.usuarioRepository.findAll().isEmpty() && this.papelRepository.findAll().isEmpty()){

				this.storageService.deleteAll();
				this.storageService.init();

				Papel roleUser = Papel.builder().nome("USER").build();
				Papel roleAdmin = Papel.builder().nome("ADMIN").build();

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

    }

}
