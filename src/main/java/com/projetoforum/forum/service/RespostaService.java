package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import org.springframework.stereotype.Service;

@Service
public interface RespostaService {
    Resposta save(Resposta resposta);
}
