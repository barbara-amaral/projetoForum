package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Topico;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TopicoService {
    Topico save(Topico topico);
    Optional<Topico> findById(String id);
    void deleteById(String id);
}
