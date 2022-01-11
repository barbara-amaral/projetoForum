package com.projetoforum.forum.model;

import com.projetoforum.forum.controller.form.CadastroUsuarioDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document(collection = "Usuario")
public class Usuario implements UserDetails {
    @Id
    private String id;
    private String nome;
    private String email;
    private String senha;
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(CadastroUsuarioDto cadastroUsuarioDto) {
        this.nome = cadastroUsuarioDto.getNome();
        this.email = cadastroUsuarioDto.getEmail();
        this.senha = cadastroUsuarioDto.getSenha();;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    public void addPerfil(Perfil perfil){
        perfis.add(perfil);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return getId().equals(usuario.getId()) && getNome().equals(usuario.getNome()) && getEmail().equals(usuario.getEmail()) && getSenha().equals(usuario.getSenha());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNome(), getEmail(), getSenha());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
