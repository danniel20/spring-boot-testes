package com.securityteste.securityspringteste.config.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.securityteste.securityspringteste.model.Foto;
import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.PapelRepository;
import com.securityteste.securityspringteste.repository.UsuarioRepository;
import com.securityteste.securityspringteste.service.storage.StorageService;
import com.securityteste.securityspringteste.utils.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
//import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
// @Profile("development")
public class InitDataBase implements CommandLineRunner {

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

		if (this.environment.equals("development")) {

			if (this.usuarioRepository.findAll().isEmpty() && this.papelRepository.findAll().isEmpty()) {

				this.storageService.deleteAll();
				this.storageService.init();

				Path tempDir = Files.createTempDirectory(Paths.get("./"), "tempDir");

				String imagem1 = "robo-teste-1";
				String imagem2 = "robo-teste-2";

				Arrays.asList(imagem1, imagem2).forEach(item -> {
					try {
						Resource resource = new UrlResource("https://robohash.org/" + item);
						Files.copy(resource.getInputStream(), tempDir.resolve(item + ".png"),
								StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						System.out.println(
								"Não foi possível baixar/ou gravar a imagem da url especificada: " + e.getMessage());
					}
				});

				Papel roleAdmin = Papel.builder().nome("ADMIN").build();
				Papel roleUser = Papel.builder().nome("USER").build();

				this.papelRepository.saveAll(Arrays.asList(roleUser, roleAdmin));

				Usuario usuario1 = Usuario.builder()
						.login("user")
						.senha(new BCryptPasswordEncoder().encode("123456"))
						.nome("User")
						.email("user@teste.com")
						.dataNascimento(LocalDate.parse("25/09/1989", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
						.build();

				usuario1.getPapeis().add(roleUser);

				Usuario usuario2 = Usuario.builder()
						.login("admin")
						.senha(new BCryptPasswordEncoder().encode("123456"))
						.nome("Admin")
						.email("admin@teste.com")
						.dataNascimento(LocalDate.parse("18/02/1982", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
						.build();

				usuario2.getPapeis().add(roleUser);
				usuario2.getPapeis().add(roleAdmin);

				try {
					Path pathImageUsuario1 = tempDir.resolve(imagem1 + ".png");
					String fileName1 = this.storageService
							.store(FileUtil.fileToMultipartFile(pathImageUsuario1.toFile()));
					usuario1.setFoto(Foto.builder().fileName(fileName1).build());

					Path pathImageUsuario2 = tempDir.resolve(imagem2 + ".png");
					String fileName2 = this.storageService
							.store(FileUtil.fileToMultipartFile(pathImageUsuario2.toFile()));
					usuario2.setFoto(Foto.builder().fileName(fileName2).build());
				} catch (Exception e) {
					System.out.println("Não foi possível salvar a image do usuário: " + e.getMessage());
				}

				this.usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2));

				FileSystemUtils.deleteRecursively(tempDir);
			}

		}

	}

}
