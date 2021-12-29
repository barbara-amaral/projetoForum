package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Topico;
import org.springframework.stereotype.Service;

@Service
public interface TopicoService {
    Topico save(Topico topico);
}
