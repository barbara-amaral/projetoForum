package com.projetoforum.forum.config.security;

import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UsuarioRepository repository;

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoTokenFilter.class);

    public AutenticacaoTokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);
        if(valido){
            log.info("Autenticando usuário...");
            autenticarCliente(token);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void autenticarCliente(String token) {
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario= repository.findById(idUsuario).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Usuário autenticado.");
    }

    private String recuperarToken(HttpServletRequest httpServletRequest){
        log.info("Verificando token...");

        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        log.info("Token encontrado.");
        return token.substring(7, token.length());
    }


}
