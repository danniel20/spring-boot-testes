package com.securityteste.securityspringteste.controller.usuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.securityteste.securityspringteste.controller.usuarios.dto.UsuarioRequest;
import com.securityteste.securityspringteste.controller.usuarios.dto.UsuarioResponse;
import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.response.ResponseHandler;
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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid UsuarioRequest usuarioRequest){

        try {
            Usuario usuarioNovo = new Usuario();
            BeanUtils.copyProperties(usuarioRequest, usuarioNovo);
            usuarioNovo.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));

            List<Papel> papeis = new ArrayList<Papel>();

            Arrays.stream(usuarioRequest.getPapeis()).forEach(papelString -> {
                Papel papel = new Papel();
                papel.setNome(papelString);
                papeis.add(papel);
            });

            usuarioNovo.setPapeis(papeis);

            Usuario saved = usuarioService.salvar(usuarioNovo);

            UsuarioResponse usuarioResponse = new UsuarioResponse();
            BeanUtils.copyProperties(saved, usuarioResponse);

            return ResponseHandler.generateResponse("Usuário cadastrado com sucesso!", HttpStatus.CREATED, usuarioResponse);
            
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Erro ao cadastrar Usuário.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id){

        try{
            Optional<Usuario> usuario = usuarioService.bucarPorId(id);
    
            if(usuario.isEmpty()){
                throw new Exception("Id do Usuário informado não existe!");
            }

            UsuarioResponse usuarioResponse = new UsuarioResponse();
            BeanUtils.copyProperties(usuario.get(), usuarioResponse);
    
            return ResponseHandler.generateResponse(null, HttpStatus.OK, usuarioResponse);
            
            
        }
        catch(Exception e){
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
                UsuarioResponse usuarioResponse = new UsuarioResponse();
                BeanUtils.copyProperties(usuario, usuarioResponse);
                listUsuariosResponse.add(usuarioResponse);
            });

            return ResponseHandler.generateResponse(null, HttpStatus.OK, listUsuariosResponse);

        } catch (Exception e) {
            return ResponseHandler.generateResponse("Erro ao listar usuários.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> deletarPorId(@PathVariable Long id){

        try {
            usuarioService.deletarPorId(id);
            return ResponseHandler.generateResponse("Usuário removido com sucesso!", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Id do Usuário informado não existe!", HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
