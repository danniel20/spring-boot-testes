package com.securityteste.securityspringteste.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.securityteste.securityspringteste.service.token.TokenService;
import com.securityteste.securityspringteste.service.usuarios.UsuarioServiceImpl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class TokenAuthenticationFilter extends OncePerRequestFilter{

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";

    private TokenService tokenService;

    private UsuarioServiceImpl usuarioServiceImpl;

    public TokenAuthenticationFilter(TokenService tokenService, UsuarioServiceImpl usuarioServiceImpl){
        this.tokenService = tokenService;
        this.usuarioServiceImpl = usuarioServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userNameToken = null;
        String token = request.getHeader(AUTH_HEADER);

        if(token != null && token.startsWith(BEARER_PREFIX)){
            token = token.substring(7, token.length());
            userNameToken = null;

            try {
                userNameToken = tokenService.getTokenSubject(token);
            } catch (ExpiredJwtException ex) {
                generateErrorResponse("Token expirado!", request, response);
                return;
            }
            catch (UnsupportedJwtException ex) {
                generateErrorResponse("Token não suportado!", request, response);
                return;
            }
            catch (MalformedJwtException | SignatureException ex) {
                generateErrorResponse("Token inválido!", request, response);
                return;
            }
        }

        if(userNameToken != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails usuario = usuarioServiceImpl.buscarPorLogin(userNameToken).get();

            if(usuario != null && this.tokenService.isTokenValid(token)){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void generateErrorResponse(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"error\": \"Unauthorized\",");
        sb.append("\"message\": \""+errorMessage+"\",");
        sb.append("\"path\": \"")
        .append(request.getRequestURL()).append("\"");
        sb.append("} ");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(sb.toString());
    }
}
