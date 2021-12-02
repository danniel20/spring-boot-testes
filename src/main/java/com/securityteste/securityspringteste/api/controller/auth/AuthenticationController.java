package com.securityteste.securityspringteste.api.controller.auth;

import javax.validation.Valid;

import com.securityteste.securityspringteste.api.controller.auth.dto.LoginDTO;
import com.securityteste.securityspringteste.api.controller.auth.dto.TokenDTO;
import com.securityteste.securityspringteste.api.utils.ResponseHandler;
import com.securityteste.securityspringteste.service.auth.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<Object> auth(@Valid @RequestBody LoginDTO loginDTO) {

        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
            
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);            

            String token = tokenService.generateToken(authentication);
            
            return ResponseHandler.generateResponse(
                null,
                HttpStatus.OK, 
                TokenDTO.builder().type("Bearer").token(token).build()
            ); 

        }
        catch(Exception e){
            return ResponseHandler.generateResponse("Usuário e/ou senha incorreto(s)!", HttpStatus.UNAUTHORIZED, null);
        }
    }

}