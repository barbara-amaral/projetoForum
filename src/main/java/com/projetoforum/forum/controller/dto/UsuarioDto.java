package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDto {

    private String nome;
    private String email;

    public UsuarioDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public static List<UsuarioDto> converter(List<Usuario> usuario){
        return usuario.stream().map(UsuarioDto::new).collect(Collectors.toList());
    }
}
