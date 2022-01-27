package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TopicoService {
    Topico save(Topico topico);
    Optional<Topico> findById(String id);
    void deleteById(String id);
    Topico getById(String id);
    List<Topico> findAll();
    List<Topico> findTopicoByTag(String tag);
    List<Topico> findTopicoByAutorNome(String nome);
    List<Topico> findAllById(String id);
}
