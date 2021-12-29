package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoServiceImpl implements TopicoService{

    @Autowired
    TopicoRepository topicoRepository;

    @Override
    public Topico save(Topico topico) {
        return topicoRepository.save(topico);
    }
}
