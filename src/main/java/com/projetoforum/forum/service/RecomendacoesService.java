package com.projetoforum.forum.service;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecomendacoesService {

    @Autowired
    TopicoService topicoService;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    public List<Topico> sugerirTopicos(Usuario usuario) {

        List<Topico> topicos = new ArrayList<>();
        List<Topico> lista = new ArrayList<>();

        Set<String> tags = topicoService.findTopicoByAutorEmail(usuario.getEmail()).stream().map(Topico::getTag).collect(Collectors.toSet());

        for (String tag : tags) {
            lista = topicoService.findTopicosByTag(tag);
        }

        for (Topico t: lista) {
            if(!(t.getAutor().getEmail()).equals((usuario.getEmail()))){
                topicos.add(t);
            }
        }

        log.info("Retornando topicos de recomendacao para "+usuario.getEmail());

        return topicos;
    }
}
