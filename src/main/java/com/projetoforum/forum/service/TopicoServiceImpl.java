package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Topico getById(String id) {
        return topicoRepository.getById(id);
    }

    @Override
    public List<Topico> findAll() {
        return topicoRepository.findAll();
    }

    @Override
    public List<Topico> findTopicoByTag(String tag) {
        return topicoRepository.findTopicoByTag(tag);
    }

    @Override
    public Topico insert(Topico topico) {
        return topicoRepository.insert(topico);
    }
}
