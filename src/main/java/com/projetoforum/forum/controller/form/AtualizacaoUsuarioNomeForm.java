package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AtualizacaoUsuarioNomeForm {

    @NotBlank
    private String novoNome;

    public String getNovoNome() {
        return novoNome;
    }

    public void setNovoNome(String novoNome) {
        this.novoNome = novoNome;
    }

    public ResponseEntity<?> atualizar(String emailUsuario, UsuarioService usuarioService){
        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        String usuarioNome = usuario.getNome();

        if(!novoNome.matches(usuarioNome)){
            usuario.setNome(novoNome);
            usuarioService.save(usuario);
            return ResponseEntity.ok().body(usuario);
        }else
            return ResponseEntity.badRequest().build();
    }
}
