package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.ResponderTopicoDto;
import com.projetoforum.forum.controller.dto.RespostaDto;
import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.StatusTopico;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.RespostaService;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/responder")
public class RespostaController {

    @Autowired
    TopicoService topicoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RespostaService respostaService;

    private static final Logger log = LoggerFactory.getLogger(TopicoController.class);


    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<RespostaDto> responder (@PathVariable(value = "id") String id, @RequestBody @Valid ResponderTopicoDto responderTopicoDto, HttpServletRequest httpServletRequest){

        log.info("Buscando tópico através do Id...");

        Topico topico = topicoService.getById(id);

        if (topico == null) {
            log.info("Tópico não foi encontrado na base de dados.");
            throw new NoSuchElementException("Tópico não encontrado");
        }

        log.info("Preparando nova resposta...");

        Resposta resposta = new Resposta(responderTopicoDto);

        log.info("Obtendo informações de data e hora...");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        resposta.setDataCriacao(LocalDateTime.now().format(formatter));

        log.info("Recuperando usuário através do token...");

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario autor = usuarioService.findById(idUsuario).get();

        resposta.setAutor(autor);
        resposta.setTopico_id(topico.getId());
        respostaService.save(resposta);

        log.info("Resposta salva na base de dados.");

        log.info("Alterando informações do tópico...");

        topico.setStatus(StatusTopico.NAO_SOLUCIONADO);
        topico.addResposta(resposta);
        topicoService.save(topico);

        log.info("Tópico salvo na base de dados.");

        return ResponseEntity.ok().body(new RespostaDto(topico, resposta));
    }

    private String recuperarToken (HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }
}
