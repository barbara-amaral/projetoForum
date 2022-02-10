package com.projetoforum.forum.controller;

import com.projetoforum.forum.controller.dto.TopicoDto;
import com.projetoforum.forum.controller.dto.AtualizacaoTopicoDto;
import com.projetoforum.forum.controller.dto.CadastroTopicoDto;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"Tópico"})
public class TopicoController {

    @Autowired
    TopicoService topicoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(TopicoController.class);

    @ApiOperation(value = "Cadastra um novo tópico.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir um cadastro, copiar o token e colar na frente da palavra Bearer.")

    @PostMapping("novotopico")
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid CadastroTopicoDto cadastroTopicoDto, HttpServletRequest httpServletRequest, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = new Topico(cadastroTopicoDto);
        log.info("Gerando novo tópico...");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        topico.setDataCriacao(LocalDateTime.now().format(formatter));
        log.info("Pegando informações de data e hora...");
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario= usuarioService.findById(idUsuario).get();
        log.info("Recuperando usuário através do token...");
        topico.setAutor(usuario);
        topicoService.save(topico);
        log.info("Tópico salvo na base de dados.");
        log.info("Tópico cadastrado.");
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @ApiOperation(value = "Deleta um tópico.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir um cadastro, copiar o token e colar na frente da palavra Bearer." +
            " Além disso, somente o autor do tópico pode deletá-lo. Você também precisará do ID do tópico que quer deletar.")

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable(value = "id") String id, HttpServletRequest httpServletRequest){

        Optional<Topico> topico = topicoService.findById(id);
        log.info("Buscando tópico através do Id...");

        if (topico.isEmpty()){
            log.info("Tópico não foi encontrado na base de dados.");
            throw new NoSuchElementException("Tópico não encontrado");
        }

        Usuario autor = topico.get().getAutor();
        log.info("Identificando autor do tópico...");
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();
        log.info("Recuperando usuário através do token...");
        log.info("Verificando usuário...");
        if(topico.isPresent() && usuarioLogado.equals(autor)){
            topicoService.deleteById(id);
            log.info("Tópico deletado.");
            return ResponseEntity.ok("Tópico deletado com sucesso.");
        }else if (usuarioLogado != autor){
            log.info("Houve um erro: Usuário não tem permissão para deletar esse tópico.");
            log.info("Não foi possível prosseguir com a operação.");
            return ResponseEntity.badRequest().body("Você não tem permissão para deletar esse tópico");
        }
            return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Atualiza um tópico.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir um cadastro, copiar o token e colar na frente da palavra Bearer." +
            " Além disso, somente o autor do tópico pode atualizá-lo. Você também precisará do ID do tópico que quer atualizar.")

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable(value = "id") String id, @RequestBody @Valid AtualizacaoTopicoDto atualizacaoTopicoDto, HttpServletRequest httpServletRequest){
        Optional<Topico> optionalTopico = topicoService.findById(id);
        log.info("Buscando tópico através do Id...");
        if (optionalTopico.isEmpty()){
            log.info("Tópico não foi encontrado na base de dados.");
            throw new NoSuchElementException("Tópico não encontrado");
        }

        Usuario autor = optionalTopico.get().getAutor();
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();
        log.info("Recuperando usuário através do token...");
        log.info("Verificando usuário...");

        if(optionalTopico.isPresent() && usuarioLogado.equals(autor)){
            Topico topico = atualizacaoTopicoDto.atualizar(id, topicoService);
            log.info("Tópico atualizado.");
            return ResponseEntity.ok(new TopicoDto(topico));
        }else if (usuarioLogado != autor){
            log.info("Houve um erro: Usuário não tem permissão para deletar esse tópico.");
            log.info("Não foi possível prosseguir com a operação.");
            return ResponseEntity.badRequest().body("Você não tem permissão para atualizar esse tópico");
        }
        return ResponseEntity.notFound().build();

    }

    @ApiOperation(value = "Lista todos os tópicos.", notes = "Esse método não necessita de autenticação." +
            " Você também pode listar por tag, se preferir.")

    @GetMapping("/listar")
    @Transactional
    public ResponseEntity<?> listar(@RequestParam(required = false) String tag){
        if (tag == null){
            log.info("Tag não informada: Retornando todos os tópicos.");
            List<Topico> topicos = topicoService.findAll();
            return ResponseEntity.ok(TopicoDto.converter(topicos));
        }else {
            log.info("Buscando tópicos com a tag informada...");
            List<Topico> topicos = topicoService.findTopicosByTag(tag);
            log.info("Retornando tópicos.");
            if(topicos.isEmpty()){
                log.info("Houve um erro: Não existem tópicos com a tag informada.");
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
