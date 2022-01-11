package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class AtualizacaoUsuarioEmailDto {

    @NotBlank
    @Email
    private String novoEmail;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    public String getNovoEmail() {
        return novoEmail;
    }

    public void setNovoEmail(String novoEmail) {
        this.novoEmail = novoEmail;
    }

    public ResponseEntity<?> atualizar(String emailUsuario, UsuarioService usuarioService){
        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        log.info("Verificando e-mail informado...");
        if(!novoEmail.matches(emailUsuario)){
            usuario.setEmail(this.novoEmail);
            log.info("Novo e-mail salvo na base de dados.");
            usuarioService.save(usuario);
            return ResponseEntity.ok().body(usuario);
        }else
            log.info("Ocorreu um erro: E-mail não pode ser o mesmo.");
            return ResponseEntity.badRequest().build();
    }
}
