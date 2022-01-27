package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.StatusTopico;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RespostaServiceImpl implements RespostaService{

    @Autowired
    RespostaRepository respostaRepository;

    @Override
    public Resposta save(Resposta resposta) {
        return respostaRepository.save(resposta);
    }

    @Override
    public Resposta getById(String id) {
        return respostaRepository.getById(id);
    }

    @Override
    public Optional<Resposta> findById(String id) {
        return respostaRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        respostaRepository.deleteById(id);
    }

    @Override
    public List<Resposta> findAll() {
        return respostaRepository.findAll();
    }

    @Override
    public List<Resposta> findRespostaByAutorNome(String nome) {
        return respostaRepository.findRespostaByAutorNome(nome);
    }

}
