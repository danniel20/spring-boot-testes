package com.securityteste.securityspringteste.controller.auth;

import javax.validation.Valid;

import com.securityteste.securityspringteste.controller.auth.dto.LoginDTO;
import com.securityteste.securityspringteste.controller.auth.dto.TokenDTO;
import com.securityteste.securityspringteste.response.ResponseHandler;
import com.securityteste.securityspringteste.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.validation.FieldError;
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> auth(@Valid @RequestBody LoginDTO loginDTO) throws Exception {

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
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

}