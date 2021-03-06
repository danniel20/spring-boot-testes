package com.securityteste.securityspringteste.api.controller.usuarios;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.securityteste.securityspringteste.api.controller.usuarios.dto.UsuarioRequest;
import com.securityteste.securityspringteste.api.controller.usuarios.dto.UsuarioResponse;
import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.api.utils.ResponseHandler;
import com.securityteste.securityspringteste.service.papeis.PapelServiceImpl;
import com.securityteste.securityspringteste.service.usuarios.UsuarioServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "apiUsuarioController")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PapelServiceImpl papelService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid UsuarioRequest usuarioRequest){

        try {

            if(usuarioService.buscarPorLogin(usuarioRequest.getLogin()).isPresent()){
                return ResponseHandler.generateResponse("J?? existe usu??rio cadastrado com esse login!", HttpStatus.CREATED, null);
            }

            Usuario usuarioNovo = Usuario.builder()
                .login(usuarioRequest.getLogin())
                .nome(usuarioRequest.getNome())
                .email(usuarioRequest.getEmail())
                .senha(passwordEncoder.encode(usuarioRequest.getSenha()))
                .dataNascimento(LocalDate.parse(usuarioRequest.getDataNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

            if(usuarioRequest.getPapeis() == null || usuarioRequest.getPapeis().length == 0){
                Papel papel = this.papelService.bucarPorNome("USER").get();
                usuarioNovo.getPapeis().add(papel);
            }
            else{
                Arrays.stream(usuarioRequest.getPapeis()).forEach(papelString -> {
                    Papel papel = this.papelService.bucarPorNome(papelString).get();
                    usuarioNovo.getPapeis().add(papel);
                });
            }

            Usuario saved = usuarioService.salvar(usuarioNovo);

            UsuarioResponse usuarioResponse = UsuarioResponse.builder().build();
            BeanUtils.copyProperties(saved, usuarioResponse);

            return ResponseHandler.generateResponse("Usu??rio cadastrado com sucesso!", HttpStatus.CREATED, usuarioResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Erro ao cadastrar Usu??rio.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id){

        try{
            Optional<Usuario> usuario = usuarioService.bucarPorId(id);

            if(usuario.isEmpty()){
                throw new Exception("Id do Usu??rio informado n??o existe!");
            }

            UsuarioResponse usuarioResponse = UsuarioResponse.builder().build();
            BeanUtils.copyProperties(usuario.get(), usuarioResponse);

            return ResponseHandler.generateResponse(null, HttpStatus.OK, usuarioResponse);
        } catch(Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> listarTodos(){

        try {
            List<Usuario> usuarios = usuarioService.buscarTodos();

            List<UsuarioResponse> listUsuariosResponse = new ArrayList<UsuarioResponse>();

            usuarios.forEach(usuario -> {
                UsuarioResponse usuarioResponse = UsuarioResponse.builder().build();
                BeanUtils.copyProperties(usuario, usuarioResponse);
                listUsuariosResponse.add(usuarioResponse);
            });

            return ResponseHandler.generateResponse(null, HttpStatus.OK, listUsuariosResponse);

        } catch (Exception e) {
            return ResponseHandler.generateResponse("Erro ao listar usu??rios.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> deletarPorId(@PathVariable Long id){

        try {
            Optional<Usuario> usuario = usuarioService.bucarPorId(id);

            if(usuario.isEmpty()){
                throw new Exception("Id do Usu??rio informado n??o existe!");
            }

            usuarioService.deletarPorId(id);
            return ResponseHandler.generateResponse("Usu??rio removido com sucesso!", HttpStatus.NO_CONTENT, null);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
