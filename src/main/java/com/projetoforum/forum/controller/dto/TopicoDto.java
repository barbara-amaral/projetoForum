package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Topico;

import java.time.LocalDateTime;

public class TopicoDto {
    private String id;
    private String titulo;
    private String mensagem;
    private String categoria;
    private LocalDateTime dataCriacao;

    public TopicoDto(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.categoria = topico.getCategoria();
        this.dataCriacao = topico.getDataCriacao();
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getCategoria() {
        return categoria;
    }
}
