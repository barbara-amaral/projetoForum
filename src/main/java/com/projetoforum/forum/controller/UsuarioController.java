package com.projetoforum.forum.controller;


import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.UsuarioDto;
import com.projetoforum.forum.controller.form.*;
import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cadastro")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = new Usuario(form);
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));

        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);

        usuarioService.save(usuario);
        log.info("Usuário cadastrado.");

        URI uri = uriComponentsBuilder.path("/cadastro/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestBody @Valid DeletarUsuarioForm form, HttpServletRequest httpServletRequest){

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        ResponseEntity<?> deletar = form.deletar(emailUsuario, usuarioService);

        if(deletar.getStatusCode() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>("Dados inválidos.",HttpStatus.BAD_REQUEST);
        }

        usuarioService.deleteUsuarioByEmail(emailUsuario);
        log.info("Usuário deletado.");
        return ResponseEntity.ok("Usuário deletado com sucesso.");

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<?> listar(@RequestParam(required = false) String email){

        if(email == null){
            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(UsuarioDto.converter(usuarios));
        }else {
            Usuario usuario = usuarioService.findUsuarioByEmail(email);
            if (usuario == null){
                return new ResponseEntity<>("Usuário não encontrado.",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(new UsuarioDto(usuario));

        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
   @PutMapping("atualizarEmail")
   public Object atualizarEmail(@RequestBody @Valid AtualizacaoUsuarioEmailForm form, AtualizacaoUsuarioNomeForm nomeForm, HttpServletRequest httpServletRequest){

       String token = recuperarToken(httpServletRequest);
       String idUsuario = tokenService.getIdUsuario(token);
       String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

       ResponseEntity<?> atualizar = form.atualizar(emailUsuario, usuarioService);

            if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
                return ResponseEntity.badRequest().body("E-mail deve ser diferente do anterior.");
            }

            log.info("E-mail atualizado.");
            return ResponseEntity.ok("E-mail atualizado com sucesso.");
   }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
   @PutMapping("/atualizarNome")
   public ResponseEntity<?> atualizarNome(@RequestBody @Valid AtualizacaoUsuarioNomeForm form, HttpServletRequest httpServletRequest){

       String token = recuperarToken(httpServletRequest);
       String idUsuario = tokenService.getIdUsuario(token);
       String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

       ResponseEntity<?> atualizar = form.atualizar(emailUsuario, usuarioService);

       if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
           return ResponseEntity.badRequest().body("Nome deve ser diferente do anterior.");
       }
       log.info("Nome atualizado.");
       return ResponseEntity.ok("Nome atualizado com sucesso.");
   }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/atualizarSenha")
    public ResponseEntity<?> atualizarSenha(@RequestBody @Valid AtualizacaoSenhaForm form, HttpServletRequest httpServletRequest){

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        ResponseEntity<?> atualizar = form.atualizar(emailUsuario, usuarioService);

        if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
            return ResponseEntity.badRequest().body("Senha deve ser diferente da anterior.");
        }
        log.info("Senha atualizada.");
        return ResponseEntity.ok("Senha atualizada com sucesso.");

    }

    private String recuperarToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
