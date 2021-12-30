package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.service.TopicoService;

import javax.validation.constraints.NotBlank;

public class AtualizacaoTopicoForm {

    @NotBlank
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Topico atualizar(String id, TopicoService topicoService){
        Topico topico = topicoService.getById(id);
        topico.setMensagem(this.mensagem);
        topicoService.save(topico);
        return topico;
    }
}
