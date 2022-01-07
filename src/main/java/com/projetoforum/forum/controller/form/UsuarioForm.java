package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Perfil;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {
    @NotBlank
    private String nome;
    @NotBlank @Email @Indexed(unique = true)
    private String email;
    @NotBlank
    private String senha;
    private List<Perfil> perfis = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public List<Perfil> setPerfis(Perfil perfil) {
        this.perfis = perfis;
        return perfis;
    }
}
