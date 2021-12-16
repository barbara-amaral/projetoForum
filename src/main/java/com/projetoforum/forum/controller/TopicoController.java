package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TopicoDto;
import com.projetoforum.forum.controller.form.TopicoForm;
import com.projetoforum.forum.repository.TopicoRepository;
import com.projetoforum.forum.model.Topico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository repository;

    @PostMapping("novotopico")
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = new Topico(form);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        topico.setDataCriacao(LocalDateTime.now().format(formatter));

        repository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

}
