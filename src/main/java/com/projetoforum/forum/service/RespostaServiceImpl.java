package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.StatusTopico;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespostaServiceImpl implements RespostaService{

    @Autowired
    RespostaRepository respostaRepository;

    @Override
    public Resposta save(Resposta resposta) {
        return respostaRepository.save(resposta);
    }

}
