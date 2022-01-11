package com.projetoforum.forum.config.security;

import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    public String gerarToken(Authentication authentication){
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExp = new Date(hoje.getTime() + Long.parseLong(expiration));

        log.info("Token gerado.");

        return Jwts.builder()
                .setIssuer("Projeto Forum")
                .setSubject(logado.getId())
                .setIssuedAt(hoje)
                .setExpiration(dataExp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {

        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            log.info("Token válido.");
            return true;
        }catch (Exception e){
            log.info("Token inválido ou inexistente.");
            return false;
        }
    }

    public String getIdUsuario(String token) {

        log.info("Recuperando Id do usuário...");

        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
