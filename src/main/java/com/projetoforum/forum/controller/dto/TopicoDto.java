package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.StatusTopico;
import com.projetoforum.forum.model.Topico;

import java.util.List;
import java.util.stream.Collectors;

public class TopicoDto {
    private String id;
    private String titulo;
    private String mensagem;
    private String tag;
    private String dataCriacao;
    private String autor;
    private StatusTopico status;

    public TopicoDto(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.tag = topico.getTag();
        this.dataCriacao = topico.getDataCriacao();
        this.autor = topico.getAutor().getNome();
        this.status = topico.getStatus();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public String getTag() {
        return tag;
    }

    public String getAutor() {
        return autor;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public static List<TopicoDto> converter(List<Topico> topico){
        return topico.stream().map(TopicoDto::new).collect(Collectors.toList());
    }
}
