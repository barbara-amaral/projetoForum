package com.projetoforum.forum.controller.form;

import javax.validation.constraints.NotBlank;

public class TopicoForm {
    @NotBlank
    private String titulo;
    @NotBlank
    private String mensagem;
    @NotBlank
    private String categoria;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
