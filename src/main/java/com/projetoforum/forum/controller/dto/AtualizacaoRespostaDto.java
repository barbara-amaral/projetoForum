package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.service.RespostaService;
import com.projetoforum.forum.service.TopicoService;

import javax.validation.constraints.NotBlank;

public class AtualizacaoRespostaDto {

    @NotBlank
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Resposta atualizar(String id, RespostaService respostaService){
        Resposta resposta = respostaService.getById(id);
        resposta.setMensagem(this.mensagem);
        respostaService.save(resposta);
        return resposta;
    }
}
