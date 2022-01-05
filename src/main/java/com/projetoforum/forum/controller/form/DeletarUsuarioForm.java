package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class DeletarUsuarioForm {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

        String senha = this.getSenha();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(email.matches(usuarioEmail) && passwordEncoder.matches(senha,usuarioSenha)){
            return ResponseEntity.ok().build();
        }else
            return ResponseEntity.badRequest().build();
    }
}
