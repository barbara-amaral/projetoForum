package com.projetoforum.forum.controller.dto;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;

import java.util.List;
import java.util.stream.Collectors;

public class ListarRespostasDto {


    private String mensagemResposta;

    private String autorResposta;

    private String dataResposta;

    public ListarRespostasDto(Resposta resposta) {


        this.mensagemResposta = resposta.getMensagem();
        this.autorResposta = resposta.getAutor().getNome();
        this.dataResposta = resposta.getDataCriacao();
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

public static List<ListarRespostasDto> converter(List<Resposta> respostas){
   return respostas.stream().map(ListarRespostasDto::new).collect(Collectors.toList());
}

}
