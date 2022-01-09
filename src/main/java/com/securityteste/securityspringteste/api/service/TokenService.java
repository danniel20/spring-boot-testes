package com.securityteste.securityspringteste.api.service;

import java.util.Date;

import com.securityteste.securityspringteste.model.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + this.expiration * 60000);
    }

    public String generateToken(Authentication authentication){

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return Jwts.builder()
                        .setIssuer("AplicacaoJWT")
                        .setSubject(usuario.getLogin())
                        .setIssuedAt(new Date())
                        .setExpiration(generateExpirationDate())
                        .signWith(SignatureAlgorithm.HS256, this.secret)
                        .compact();

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(this.secret)
            .parseClaimsJws(token)
            .getBody();
    }

    public String getTokenSubject(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Date getExpirationDate(String token){
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    private boolean isTokenExpired(String token){
        final Date dataExpiracao = getExpirationDate(token);
        return dataExpiracao.before(new Date());
    }

    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }
}
