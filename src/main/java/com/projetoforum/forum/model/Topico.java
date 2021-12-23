package com.projetoforum.forum.model;

import com.projetoforum.forum.controller.form.TopicoForm;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "Topico")
public class Topico {

    @Id
    private String id;
    private Usuario autor;
    private String titulo;
    private String mensagem;
    private String dataCriacao;
    private StatusTopico status = StatusTopico.NAO_RESPONDIDO;
    private String tag;
    private List<Resposta> resposta = new ArrayList<>();


    public Topico() {
    }

    public Topico(TopicoForm form) {
        this.titulo = form.getTitulo();
        this.mensagem = form.getMensagem();
        this.tag = form.getTag();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public void setStatus(StatusTopico status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Resposta> getResposta() {
        return resposta;
    }

    public void setResposta(List<Resposta> resposta) {
        this.resposta = resposta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topico)) return false;
        Topico topico = (Topico) o;
        return getId().equals(topico.getId()) && getAutor().equals(topico.getAutor()) && getTitulo().equals(topico.getTitulo()) && getMensagem().equals(topico.getMensagem()) && getDataCriacao().equals(topico.getDataCriacao()) && getStatus() == topico.getStatus() && getTag().equals(topico.getTag()) && Objects.equals(getResposta(), topico.getResposta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAutor(), getTitulo(), getMensagem(), getDataCriacao(), getStatus(), getTag(), getResposta());
    }
}
