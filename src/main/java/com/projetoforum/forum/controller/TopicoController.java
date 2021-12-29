package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TopicoDto;
import com.projetoforum.forum.controller.form.TopicoForm;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.UsuarioRepository;
import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoService topicoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenService tokenService;

    @PostMapping("novotopico")
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, HttpServletRequest httpServletRequest, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = new Topico(form);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        topico.setDataCriacao(LocalDateTime.now().format(formatter));

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario= usuarioService.findById(idUsuario).get();

        topico.setAutor(usuario);

        topicoService.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    private String recuperarToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }

}
