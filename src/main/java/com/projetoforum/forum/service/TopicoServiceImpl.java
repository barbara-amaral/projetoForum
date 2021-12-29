package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoServiceImpl implements TopicoService{

    @Autowired
    TopicoRepository topicoRepository;

    @Override
    public Topico save(Topico topico) {
        return topicoRepository.save(topico);
    }

    @Override
    public Optional<Topico> findById(String id) {
        return topicoRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        topicoRepository.deleteById(id);
    }
}
