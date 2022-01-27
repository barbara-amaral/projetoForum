package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TokenDto;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.config.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginDto loginDto){

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        try{
            log.info("Preparando para autenticar usuário...");
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            log.info("Gerando token...");
            String token = tokenService.gerarToken(authentication);
            log.info("Token gerado.");
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        }catch (AuthenticationException e){
            log.info("Ocorreu um erro: os dados informados estão incorretos.");
            return ResponseEntity.badRequest().body("Dados inválidos.");
        }

    }

}
