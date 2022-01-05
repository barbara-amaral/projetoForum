package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class AtualizacaoUsuarioEmailForm {

    @NotBlank
    @Email
    private String novoEmail;

    public String getNovoEmail() {
        return novoEmail;
    }

    public void setNovoEmail(String novoEmail) {
        this.novoEmail = novoEmail;
    }

    public ResponseEntity<?> atualizar(String emailUsuario, UsuarioService usuarioService){
        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        if(!novoEmail.matches(emailUsuario)){
            usuario.setEmail(this.novoEmail);
            usuarioService.save(usuario);
            return ResponseEntity.ok().body(usuario);
        }else
            return ResponseEntity.badRequest().build();
    }
}
