package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TopicoDto;
import com.projetoforum.forum.controller.form.TopicoForm;
import com.projetoforum.forum.repository.TopicoRepository;
import com.projetoforum.forum.model.Topico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository repository;

    @PostMapping("novotopico")
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody TopicoForm form){
        Topico topico = new Topico(form);
        repository.save(topico);
        return ResponseEntity.ok().body(new TopicoDto(topico));
    }

}
