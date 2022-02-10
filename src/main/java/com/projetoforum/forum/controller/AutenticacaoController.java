package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TokenDto;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.config.security.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "login", tags = {"Login"})
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @ApiOperation(value = "Faz o login na aplicacão e autentica o usuário.", notes = "Para a autenticação é utilizado o token JWT," +
            " e a autenticação é do tipo Bearer. Você precisará desse token para testar métodos " +
            "que precisam de autenticação. Basta copiar o token que é devolvido e colar na frente da palavra " +
            "Bearer quando necessário. Para se autenticar, você precisa ter um cadastro antes. Se quiser testar métodos " +
            "restritos, como o de listar os usuários cadastrados, seu usuário precisa ser um ADMIN. Para isso, basta " +
            " cadastrar com um e-mail que termine com \"@admin.com.\"")

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
