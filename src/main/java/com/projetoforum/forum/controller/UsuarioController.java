package com.projetoforum.forum.controller;


import com.projetoforum.forum.controller.dto.UsuarioDto;
import com.projetoforum.forum.controller.form.UsuarioForm;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cadastro")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = new Usuario(form);
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        repository.save(usuario);


        URI uri = uriComponentsBuilder.path("/cadastro/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
    }



}
