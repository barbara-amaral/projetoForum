package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Topico;

import java.util.List;
import java.util.stream.Collectors;

public class TopicoDto {
    private String titulo;
    private String mensagem;
    private String tag;
    private String dataCriacao;

    public TopicoDto(Topico topico) {
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.tag = topico.getTag();
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

    public String getTag() {
        return tag;
    }

    public static List<TopicoDto> converter(List<Topico> topico){
        return topico.stream().map(TopicoDto::new).collect(Collectors.toList());
    }
}
