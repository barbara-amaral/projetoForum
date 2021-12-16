package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Topico;

public class TopicoDto {
    private String titulo;
    private String mensagem;
    private String categoria;
    private String dataCriacao;

    public TopicoDto(Topico topico) {
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.categoria = topico.getCategoria();
        this.dataCriacao = topico.getDataCriacao();
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

    public String getCategoria() {
        return categoria;
    }
}
