package com.securityteste.securityspringteste.controller;

import java.util.Arrays;

import com.securityteste.securityspringteste.controller.usuarios.UsuarioController;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.service.auth.TokenService;
import com.securityteste.securityspringteste.service.auth.UserDetailsServiceImpl;
import com.securityteste.securityspringteste.service.usuarios.UsuarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioServiceImpl usuarioService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private TokenService tokenService;

    @BeforeEach
    public void setup(){
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveObterTodosUsuarios() throws Exception {

        Mockito.when(usuarioService.buscarTodos()).thenReturn(
            Arrays.asList(
                new Usuario("joao", "123456", "João das Neves", "joao@teste.com", null),
                new Usuario("ana", "123456", "Ana Maria", "ana@teste.com", null),
                new Usuario("jose", "123456", "José Silva", "jose@teste.com", null)
            )
        );
        
        mockMvc.perform(MockMvcRequestBuilders
                            .get("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data", hasSize(3)));
        
        // MockHttpServletResponse response = mockMvc.perform(
        //         MockMvcRequestBuilders
        //             .get("/usuarios")
        //             .contentType(MediaType.APPLICATION_JSON))
        //             .andReturn().getResponse();

        // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
}
