package com.projetoforum.forum.controller;


import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.UsuarioDto;
import com.projetoforum.forum.controller.form.AtualizacaoUsuarioForm;
import com.projetoforum.forum.controller.form.UsuarioForm;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.repository.UsuarioRepository;
import com.projetoforum.forum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/cadastro")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = new Usuario(form);
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        usuarioService.save(usuario);


        URI uri = uriComponentsBuilder.path("/cadastro/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
    }

    @DeleteMapping("/deletar/{email}")
    public ResponseEntity<?> deletar(@PathVariable(value = "email") String email, HttpServletRequest httpServletRequest){
        Optional<Usuario> usuario = usuarioService.findByEmail(email);

        if(usuario.isEmpty()){
            throw new NoSuchElementException("Usuário não encontrado.");
        }

        String emailUsuario = usuario.get().getEmail();

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        String usuarioLogado= usuarioService.findById(idUsuario).get().getEmail();

        if(emailUsuario != null && usuarioLogado.matches(emailUsuario)){
            usuarioService.deleteUsuarioByEmail(email);
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        }else if (!usuarioLogado.matches(emailUsuario)){
            return ResponseEntity.badRequest().body("Você não tem permissão para deletar esse usuário.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){

            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(UsuarioDto.converter(usuarios));

    }

   @PutMapping("atualizar/{email}")
   public ResponseEntity<?> atualizarEmail(@PathVariable(value = "email") String email, @RequestBody @Valid AtualizacaoUsuarioForm form, HttpServletRequest httpServletRequest){
        Optional<Usuario> optionalUsuario = usuarioService.findByEmail(email);
        if(optionalUsuario.isEmpty()){
            throw new NoSuchElementException("Usuário não encontrado.");
        }
       String emailUsuario = optionalUsuario.get().getEmail();

       String token = recuperarToken(httpServletRequest);
       String idUsuario = tokenService.getIdUsuario(token);
       String usuarioLogado= usuarioService.findById(idUsuario).get().getEmail();

       if(emailUsuario != null && usuarioLogado.matches(emailUsuario)){
           ResponseEntity<?> atualizar = form.atualizar(emailUsuario, usuarioService);
           if(atualizar.getStatusCode() == HttpStatus.OK){
               Usuario usuario = (Usuario) atualizar.getBody();
               return ResponseEntity.ok(new UsuarioDto(usuario));
           }else if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
               return ResponseEntity.badRequest().body("E-mail deve ser diferente do anterior.");
           }


      }else if (!usuarioLogado.matches(emailUsuario)){
          return ResponseEntity.badRequest().body("Você não tem permissão para atualizar esse usuário.");
      }
       return ResponseEntity.notFound().build();
   }

    private String recuperarToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
