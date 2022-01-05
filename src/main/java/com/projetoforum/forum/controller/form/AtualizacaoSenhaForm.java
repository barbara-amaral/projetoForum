package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AtualizacaoSenhaForm {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

        if(!passwordEncoder.matches(senha,usuarioSenha)){
            usuario.setSenha(passwordEncoder.encode(this.getNovaSenha()));
            usuarioService.save(usuario);
            return ResponseEntity.ok().body(usuario);
        }else
            return ResponseEntity.badRequest().build();
    }
}
