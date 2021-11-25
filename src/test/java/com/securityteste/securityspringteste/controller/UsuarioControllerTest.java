package com.securityteste.securityspringteste.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityteste.securityspringteste.controller.usuarios.UsuarioController;
import com.securityteste.securityspringteste.controller.usuarios.dto.UsuarioRequest;
import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.service.auth.TokenService;
import com.securityteste.securityspringteste.service.auth.UserDetailsServiceImpl;
import com.securityteste.securityspringteste.service.usuarios.UsuarioServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<Usuario> usuariosList;

    @BeforeEach
    public void setup(){
        Usuario usuario1 = new Usuario("joao", "123456", "João das Neves", "joao@teste.com", null);
        Usuario usuario2 = new Usuario("ana", "123456", "Ana Maria", "ana@teste.com", null);
        Usuario usuario3 = new Usuario("jose", "123456", "José Silva", "jose@teste.com", null);
        
        usuario1.setId(1L);
        usuario2.setId(2L);
        usuario3.setId(3L);

        this.usuariosList = new ArrayList<Usuario>();
        this.usuariosList.addAll(Arrays.asList(usuario1, usuario2, usuario3));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveObterTodosUsuarios() throws Exception {

        Mockito.when(usuarioService.buscarTodos()).thenReturn(this.usuariosList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .get("/usuarios")
            .contentType(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", notNullValue()))
            .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveObterUsuarioPorId() throws Exception {

        Mockito.when(usuarioService.bucarPorId(this.usuariosList.get(0).getId())).thenReturn(java.util.Optional.of(this.usuariosList.get(0)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .get("/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", notNullValue()))
            .andExpect(jsonPath("$.data.nome", is("João das Neves")));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void deveCriarUsuario() throws Exception {

        UsuarioRequest usuarioRequest = new UsuarioRequest("alex", "123456", "Alex de Souza", "alex@teste.com", new String[]{"USER"});
        
        Usuario novo = new Usuario();
        BeanUtils.copyProperties(usuarioRequest, novo, "papeis");
        novo.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        
        Set<Papel> papeis = new HashSet<Papel>();

        if(usuarioRequest.getPapeis() == null || usuarioRequest.getPapeis().length == 0){
            Papel papel = new Papel();
            papel.setNome("USER");
            papeis.add(papel);
        }
        else{
            Arrays.stream(usuarioRequest.getPapeis()).forEach(papelString -> {
                Papel papel = new Papel();
                papel.setNome(papelString);
                papeis.add(papel);
            });
        }

        novo.setPapeis(papeis);

        Mockito.when(usuarioService.salvar(novo)).thenReturn(novo);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuarioRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.nome", is("Alex de Souza")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deveRemoverUsuarioPorId() throws Exception {
        
        Mockito.when(usuarioService.bucarPorId(this.usuariosList.get(0).getId())).thenReturn(java.util.Optional.of(this.usuariosList.get(0)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .delete("/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.data", nullValue()));
    }

    @AfterEach
    public void teardown(){
        this.usuariosList.clear();
    }
}
