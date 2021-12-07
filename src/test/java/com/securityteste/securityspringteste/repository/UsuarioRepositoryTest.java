package com.securityteste.securityspringteste.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = {
    "logging.level.ROOT=WARN",
    "logging.level.org.springframework.test.context.transaction=INFO",
    "logging.level.org.hibernate.SQL=DEBUG"
})
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    // @Autowired
    // private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    private Usuario usuario;

    @BeforeEach
    public void setup(){
        Papel roleUser = Papel.builder().nome("USER").build();

        Papel papelUser = this.papelRepository.saveAndFlush(roleUser);

        this.usuario = Usuario.builder()
                                    .login("fulano")
                                    .senha("123456")
                                    .email("fulano@teste.com")
                                    .nome("Fulano da Silva")
                                    .dataNascimento(LocalDate.parse("19/08/1987", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                    .build();

        this.usuario.getPapeis().add(papelUser);

        this.usuario = this.usuarioRepository.saveAndFlush(this.usuario);  
    }

    @Test
    public void deveRetornarUsuarioAoBuscarPorLoginValido(){
        Optional<Usuario> encontrado = this.usuarioRepository.findByLogin(this.usuario.getLogin());

        assertThat(encontrado.get().getId()).isNotNull();
        assertThat(encontrado.get().getLogin()).isEqualTo(this.usuario.getLogin());
    }
    
    @Test
    public void deveRetornarTrueAoRemoverUsuarioExistente(){
        this.usuarioRepository.delete(this.usuario);

        Optional<Usuario> encontrado = this.usuarioRepository.findById(this.usuario.getId());

        assertThat(encontrado.isEmpty()).isTrue();
    }

    @AfterEach
    public void teardown(){
        this.usuario = null;
        this.usuarioRepository.deleteAll();
        this.papelRepository.deleteAll();
    }
    
}
