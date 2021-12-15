package com.projetoforum.forum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "Resposta")
public class Resposta {
    @Id
    private String id;
    private String mensagem;
    private Topico topico;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private Usuario autor;
    private Boolean solucao = false;

    public Resposta() {
    }

    public Resposta(String id, String mensagem, Topico topico, LocalDateTime dataCriacao, Usuario autor, Boolean solucao) {
        this.id = id;
        this.mensagem = mensagem;
        this.topico = topico;
        this.dataCriacao = dataCriacao;
        this.autor = autor;
        this.solucao = solucao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Boolean getSolucao() {
        return solucao;
    }

    public void setSolucao(Boolean solucao) {
        this.solucao = solucao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resposta)) return false;
        Resposta resposta = (Resposta) o;
        return getId().equals(resposta.getId()) && getMensagem().equals(resposta.getMensagem()) && getTopico().equals(resposta.getTopico()) && Objects.equals(getDataCriacao(), resposta.getDataCriacao()) && getAutor().equals(resposta.getAutor()) && Objects.equals(getSolucao(), resposta.getSolucao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMensagem(), getTopico(), getDataCriacao(), getAutor(), getSolucao());
    }
}
