package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RespostaService {
    Resposta save(Resposta resposta);
    Resposta getById(String id);
    Optional<Resposta> findById(String id);
    void deleteById(String id);
    List<Resposta> findAll();
    List<Resposta> findRespostaByAutorNome(String nome);
}
