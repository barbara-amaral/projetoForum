package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.AtualizacaoRespostaDto;
import com.projetoforum.forum.controller.dto.ListarRespostasDto;
import com.projetoforum.forum.controller.dto.ResponderTopicoDto;
import com.projetoforum.forum.controller.dto.RespostaDto;
import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.StatusTopico;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.RespostaService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/")
@Api(value = "resposta", tags = {"Resposta"})
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

    @ApiOperation(value = "Responde um tópico.", notes = "Esse método necessita de autenticação.")


    @PostMapping("/topico/responder/{id}")
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

    @ApiOperation(value = "Atualiza uma resposta.", notes = "Esse método necessita de autenticação. Além disso, você precisará do ID da resposta que quer editar.")

    @PutMapping("/topico/resposta/editar/{id}")
    @Transactional
    public ResponseEntity<?> editarResposta (@PathVariable(value = "id") String id, @RequestBody @Valid AtualizacaoRespostaDto atualizacaoRespostaDto, HttpServletRequest httpServletRequest){
        log.info("Buscando resposta através do Id...");

        Optional<Resposta> optionalResposta = respostaService.findById(id);

        if (optionalResposta.isEmpty()) {
            log.info("Resposta não foi encontrada na base de dados.");
            throw new NoSuchElementException("Resposta não encontrada");
        }

        Usuario autor = optionalResposta.get().getAutor();
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();
        Topico topico = topicoService.getById(optionalResposta.get().getTopico_id());

        log.info("Recuperando usuário através do token...");
        log.info("Verificando usuário...");

        if (!usuarioLogado.equals(autor)){
            log.info("Houve um erro: Usuário não tem permissão para editar essa resposta.");
            log.info("Não foi possível prosseguir com a operação.");
            return ResponseEntity.badRequest().body("Você não tem permissão para atualizar essa resposta");
        }

        Resposta resposta = atualizacaoRespostaDto.atualizar(id, respostaService);
        log.info("Resposta atualizada.");

        log.info("Alterando informações do tópico...");
        topico.removeResposta();
        topico.addResposta(resposta);
        topicoService.save(topico);

        log.info("Tópico salvo na base de dados.");

        return ResponseEntity.ok().body(new RespostaDto(topico, resposta));

    }

    @ApiOperation(value = "Deleta uma resposta.", notes = "Esse método necessita de autenticação. Além disso, você precisará do ID da resposta que quer deletar.")

    @DeleteMapping("/topico/resposta/deletarresposta/{id}")
    @Transactional
    public ResponseEntity<?> deletarResposta(@PathVariable(value = "id") String id, HttpServletRequest httpServletRequest){

        Optional<Resposta> optionalResposta = respostaService.findById(id);
        log.info("Buscando resposta através do Id...");

        if (optionalResposta.isEmpty()){
            log.info("Resposta não foi encontrada na base de dados.");
            throw new NoSuchElementException("Resposta não encontrada");
        }

        Usuario autor = optionalResposta.get().getAutor();
        log.info("Identificando autor...");
        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        Usuario usuarioLogado= usuarioService.findById(idUsuario).get();
        Topico topico = topicoService.getById(optionalResposta.get().getTopico_id());

        log.info("Recuperando usuário através do token...");
        log.info("Verificando usuário...");

         if (!usuarioLogado.equals(autor)){
            log.info("Houve um erro: Usuário não tem permissão para deletar essa resposta.");
            log.info("Não foi possível prosseguir com a operação.");
            return ResponseEntity.badRequest().body("Você não tem permissão para deletar essa resposta");
        }

        log.info("Alterando informações do tópico...");
        topico.removeRespostaPorId(id);

        if (topico.getRespostas().isEmpty()){
            topico.setStatus(StatusTopico.NAO_RESPONDIDO);
        }

        topicoService.save(topico);

        log.info("Tópico salvo na base de dados.");


        respostaService.deleteById(id);
        log.info("Resposta deletada.");
        return ResponseEntity.ok("Resposta deletada com sucesso.");
    }

    @ApiOperation(value = "Lista as respostas de  um tópico.", notes = "Esse método não necessita de autenticação." +
            " Você precisará do ID do tópico que quer listar as respostas.")


    @GetMapping("/topico/respostas/listar/{id}")
    @Transactional
    public ResponseEntity<?> listar(@PathVariable(value = "id") String id){

        log.info("Buscando tópico através do Id...");

        Topico topico = topicoService.getById(id);

        if (topico == null) {
            log.info("Tópico não foi encontrado na base de dados.");
            throw new NoSuchElementException("Tópico não encontrado");
        }

        log.info("Retornando respostas do topico selecionado...");

            List<Resposta> respostas = topico.getRespostas();

            if(respostas.isEmpty()){
                return new ResponseEntity<>("Não existem respostas para o topico selecionado",HttpStatus.NOT_FOUND);
            }

        return ResponseEntity.ok(ListarRespostasDto.converter(respostas));

        }


        private String recuperarToken (HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }
}
