package com.projetoforum.forum.controller.dto;

import javax.validation.constraints.NotBlank;

public class CadastroTopicoDto {
    @NotBlank
    private String titulo;
    @NotBlank
    private String mensagem;
    @NotBlank
    private String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String categoria) {
        this.tag = categoria;
    }

}
