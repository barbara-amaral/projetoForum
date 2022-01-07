package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TopicoDto;
import com.projetoforum.forum.controller.form.AtualizacaoTopicoForm;
import com.projetoforum.forum.controller.form.TopicoForm;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.repository.UsuarioRepository;
import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoService topicoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(TopicoController.class);

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
        log.info("Tópico cadastrado.");
        topicoService.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable(value = "id") String id, HttpServletRequest httpServletRequest){

        Optional<Topico> topico = topicoService.findById(id);

        if (topico.isEmpty()){
            throw new NoSuchElementException("Tópico não encontrado");
        }

        Usuario autor = topico.get().getAutor();
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();


        if(topico.isPresent() && usuarioLogado.equals(autor)){
            topicoService.deleteById(id);
            log.info("Tópico deletado.");
            return ResponseEntity.ok("Tópico deletado com sucesso.");
        }else if (usuarioLogado != autor){
            return ResponseEntity.badRequest().body("Você não tem permissão para deletar esse tópico");
        }
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable(value = "id") String id, @RequestBody @Valid AtualizacaoTopicoForm form, HttpServletRequest httpServletRequest){
        Optional<Topico> optionalTopico = topicoService.findById(id);

        if (optionalTopico.isEmpty()){
            throw new NoSuchElementException("Tópico não encontrado");
        }

        Usuario autor = optionalTopico.get().getAutor();
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();


        if(optionalTopico.isPresent() && usuarioLogado.equals(autor)){
            Topico topico = form.atualizar(id, topicoService);
            log.info("Tópico atualizado.");
            return ResponseEntity.ok(new TopicoDto(topico));
        }else if (usuarioLogado != autor){
            return ResponseEntity.badRequest().body("Você não tem permissão para atualizar esse tópico");
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/listar")
    @Transactional
    public ResponseEntity<?> listar(@RequestParam(required = false) String tag){
        if (tag == null){
            List<Topico> topicos = topicoService.findAll();
            return ResponseEntity.ok(TopicoDto.converter(topicos));
        }else {
            List<Topico> topicos = topicoService.findTopicoByTag(tag);
            if(topicos.isEmpty()){
                return new ResponseEntity<>("Não existem tópicos cadastrados com a tag " + tag + ".",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(TopicoDto.converter(topicos));
        }
    }

    private String recuperarToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
