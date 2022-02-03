package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.service.TopicoService;
import org.springframework.beans.factory.annotation.Autowired;

public class RespostaDto {

    private String id;

    private String tituloTopico;

    private String dataTopico;

    private String autorTopico;

    private String mensagemResposta;

    private String autorResposta;

    private String dataResposta;

    public RespostaDto(Topico topico, Resposta resposta) {

        this.id = resposta.getId();
        this.tituloTopico = topico.getTitulo();
        this.dataTopico = topico.getDataCriacao();
        this.autorTopico = topico.getAutor().getNome();
        this.mensagemResposta = resposta.getMensagem();
        this.autorResposta = resposta.getAutor().getNome();
        this.dataResposta = resposta.getDataCriacao();
    }

    public String getId() {
        return id;
    }

    public String getTituloTopico() {
        return tituloTopico;
    }

    public String getDataTopico() {
        return dataTopico;
    }

    public String getAutorTopico() {
        return autorTopico;
    }

    public String getMensagemResposta() {
        return mensagemResposta;
    }

    public String getAutorResposta() {
        return autorResposta;
    }

    public String getDataResposta() {
        return dataResposta;
    }

}
