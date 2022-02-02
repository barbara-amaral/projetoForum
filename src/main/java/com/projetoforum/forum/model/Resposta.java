package com.projetoforum.forum.model;

import com.projetoforum.forum.controller.dto.ResponderTopicoDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "Resposta")
public class Resposta {
    @Id
    private String id;
    private String mensagem;
    private String topico_id;
    private String dataCriacao;
    private Usuario autor;
    private Boolean solucao = false;

    public Resposta() {

    }

    public Resposta(ResponderTopicoDto responderTopicoDto) {
        this.mensagem = responderTopicoDto.getMensagem();
    }

    public Resposta(Resposta resposta) {
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

    public String getTopico_id() {
        return topico_id;
    }

    public void setTopico_id(String topico_id) {
        this.topico_id = topico_id;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
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
        return getId().equals(resposta.getId()) && getMensagem().equals(resposta.getMensagem()) && getTopico_id().equals(resposta.getTopico_id()) && getDataCriacao().equals(resposta.getDataCriacao()) && getAutor().equals(resposta.getAutor()) && getSolucao().equals(resposta.getSolucao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMensagem(), getTopico_id(), getDataCriacao(), getAutor(), getSolucao());
    }
}
