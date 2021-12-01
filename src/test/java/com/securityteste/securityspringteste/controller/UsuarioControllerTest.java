package com.securityteste.securityspringteste.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
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
        Usuario usuarioMock1 = new Usuario("joao", "123456", "João das Neves", "joao@teste.com", LocalDate.of(1995, 3, 7), null);
        Usuario usuarioMock2 = new Usuario("ana", "123456", "Ana Maria", "ana@teste.com", LocalDate.of(1980, 4, 10), null);
        Usuario usuarioMock3 = new Usuario("jose", "123456", "José Silva", "jose@teste.com", LocalDate.of(1988, 10, 21), null);

        usuarioMock1.setId(1L);
        usuarioMock2.setId(2L);
        usuarioMock3.setId(3L);

        this.usuariosList = new ArrayList<Usuario>(Arrays.asList(usuarioMock1, usuarioMock2, usuarioMock3));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveRetornarStatus200AoObterTodosUsuarios() throws Exception {

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
    public void deveRetornarStatus200AoObterUsuarioPorIdValido() throws Exception {

        Mockito.when(usuarioService.bucarPorId(1L)).thenReturn(java.util.Optional.of(this.usuariosList.get(0)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .get("/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", notNullValue()))
            .andExpect(jsonPath("$.data.nome", is("João das Neves")));
    }
    
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveRetornarStatus400AoObterUsuarioPorIdInvalido() throws Exception {

        Mockito.when(usuarioService.bucarPorId(-1L)).thenReturn(java.util.Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .get("/usuarios/-1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data", nullValue()))
            .andExpect(jsonPath("$.message", is("Id do Usuário informado não existe!")));
    }
    
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void deveRetornarStatus400AoObterUsuarioPorIdNull() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .get("/usuarios/null")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data", nullValue()))
            .andExpect(jsonPath("$.message", is("Não foi possível converter a String para valor numérico!")));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void deveRetornarStatus201AoCriarUsuarioValido() throws Exception {
        
        Usuario saved = Usuario.builder()
            .login("alex")
            .senha(passwordEncoder.encode("123456"))
            .nome("Alex de Souza")
            .email("alex@teste.com")
            .dataNascimento(LocalDate.parse("19/08/1987", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .papeis(new HashSet<Papel>(){{add(Papel.builder().nome("USER").build());}})
            .build();
        
        saved.setId(1L);

        UsuarioRequest usuarioRequest = new UsuarioRequest("alex", "123456", "Alex de Souza", "alex@teste.com", "15/08/1991", new String[]{"USER"});
        
        Usuario novo = Usuario.builder()
            .login(usuarioRequest.getLogin())
            .senha(passwordEncoder.encode(usuarioRequest.getSenha()))
            .nome(usuarioRequest.getNome())
            .email(usuarioRequest.getEmail())
            .dataNascimento(LocalDate.parse(usuarioRequest.getDataNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .papeis(new HashSet<Papel>(){{add(Papel.builder().nome("USER").build());}})
            .build();

        Mockito.when(usuarioService.salvar(novo)).thenReturn(saved);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuarioRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.nome", is("Alex de Souza")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deveRetornarSatus204AoRemoverUsuarioPorIdValido() throws Exception {
        
        Mockito.when(usuarioService.bucarPorId(this.usuariosList.get(0).getId())).thenReturn(java.util.Optional.of(this.usuariosList.get(0)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .delete("/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.data", nullValue()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void deveRetornarSatus400AoRemoverUsuarioPorIdInValido() throws Exception {
        
        Mockito.when(usuarioService.bucarPorId(-1L)).thenReturn(java.util.Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
            .delete("/usuarios/-1")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data", nullValue()))
            .andExpect(jsonPath("$.message", is("Id do Usuário informado não existe!")));
    }

    @AfterEach
    public void teardown(){
        this.usuariosList.clear();
    }
}
