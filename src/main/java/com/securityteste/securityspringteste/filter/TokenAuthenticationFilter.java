package com.securityteste.securityspringteste.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.repository.UsuarioRepository;
import com.securityteste.securityspringteste.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TokenAuthenticationFilter extends OncePerRequestFilter{

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";

    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;

    public TokenAuthenticationFilter(TokenService tokenService, UsuarioRepository usuarioRepository){
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String tokenFromHeader = getTokenFromHeader(request);
        
        try{
            boolean validToken = this.tokenService.isTokenValid(tokenFromHeader);

            if(validToken){
                this.authenticate(tokenFromHeader);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        filterChain.doFilter(request, response);
        
    }

    private String getTokenFromHeader(HttpServletRequest request){        
        String token = request.getHeader(AUTH_HEADER);

        if(token == null || token.isEmpty() || !token.startsWith(BEARER_PREFIX)){
            return null;
        }

        return token.substring(7, token.length());
    }

    private void authenticate(String token){
        Long id = this.tokenService.getTokenId(token);

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if(optionalUsuario.isPresent()){
            Usuario usuario = optionalUsuario.get();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPapeis());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
    
}
