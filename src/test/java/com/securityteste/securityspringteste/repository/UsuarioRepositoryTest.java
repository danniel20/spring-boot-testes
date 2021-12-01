package com.securityteste.securityspringteste.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setup(){
        this.usuario = Usuario.builder()
                                    .login("fulano")
                                    .senha("123456")
                                    .email("fulano@teste.com")
                                    .nome("Fulano da Silva")
                                    .dataNascimento(LocalDate.parse("19/08/1987", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                    .papeis(new HashSet<Papel>(){{add(Papel.builder().nome("USER").build());}})
                                    .build();
        
    }

    @Test
    public void deveRetornarUsuarioPorLogin(){
        
        this.entityManager.persistAndFlush(this.usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByLogin(this.usuario.getLogin());

        assertThat(encontrado.get().getId()).isNotNull();
        assertThat(encontrado.get().getLogin()).isEqualTo(this.usuario.getLogin());
    }
    
    @Test
    public void deveRetornarTrueAoRemoverUsuario(){
        
        Usuario usuarioNovo = this.entityManager.persistAndFlush(this.usuario);

        this.usuarioRepository.delete(usuarioNovo);

        Optional<Usuario> encontrado = usuarioRepository.findById(usuarioNovo.getId());

        assertThat(encontrado.isEmpty()).isTrue();
    }

    @AfterEach
    public void teardown(){
        this.usuario = null;
    }
    
}
