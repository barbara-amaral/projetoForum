package com.projetoforum.forum.controller.form;

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

public class AtualizacaoSenhaDto {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @NotBlank
    private String novaSenha;

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public ResponseEntity<?> atualizar(String emailUsuario, UsuarioService usuarioService){

        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        String usuarioSenha = usuario.getSenha();

        String senha = this.getNovaSenha();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        log.info("Comparando a senha antiga com a nova senha...");

        if(!passwordEncoder.matches(senha,usuarioSenha)){
            usuario.setSenha(passwordEncoder.encode(this.getNovaSenha()));
            usuarioService.save(usuario);
            log.info("Nova senha salva na base de dados.");
            return ResponseEntity.ok().body(usuario);
        }else
            log.info("Ocorreu um erro: A senha fornecida deve ser diferente da senha antiga.");
            return ResponseEntity.badRequest().build();
    }
}
