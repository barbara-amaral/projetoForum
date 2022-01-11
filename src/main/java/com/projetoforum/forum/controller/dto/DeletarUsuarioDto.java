package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class DeletarUsuarioDto {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ResponseEntity<?> deletar(String emailUsuario, UsuarioService usuarioService){

        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        String usuarioEmail = usuario.getEmail();
        String usuarioSenha = usuario.getSenha();

        log.info("Email e senha do usuário recuperados a partir da base de dados.");

        String senha = this.getSenha();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        log.info("Verificando email e senha informados...");

        if(email.matches(usuarioEmail) && passwordEncoder.matches(senha,usuarioSenha)){
            log.info("Email e senha corretos.");
            return ResponseEntity.ok().build();
        }else
            log.info("Email ou senha incorretos.");
            return ResponseEntity.badRequest().build();
    }
}
