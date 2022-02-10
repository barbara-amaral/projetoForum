package com.projetoforum.forum.controller;


import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.*;
import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.EmailService;
import com.projetoforum.forum.service.UsuarioService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cadastro")
@Api(value = "usuario", tags = {"Usuário"})
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @ApiOperation(value = "Faz o cadastro de um novo usuário.", notes = "Esse método não necessita de autenticação." +
            " Se quiser testar métodos restritos, como o de listar os usuários cadastrados, seu usuário precisa ser um ADMIN. " +
            "Para isso, basta cadastrar com um e-mail que termine com \"@admin.com.\"")

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid CadastroUsuarioDto cadastroUsuarioDto, UriComponentsBuilder uriComponentsBuilder) throws IOException, MessagingException, TemplateException {
        Usuario usuario = new Usuario(cadastroUsuarioDto);

        log.info("Criando um novo usuário...");

        usuario.setSenha(passwordEncoder.encode(cadastroUsuarioDto.getSenha()));

        log.info("Senha gerada.");

        String email = cadastroUsuarioDto.getEmail();

        usuario.setEmail(email);

        if (email.endsWith("@admin.com")){
            Perfil perfil = new Perfil();
            perfil.setNome("ROLE_ADMIN");
            usuario.addPerfil(perfil);
        }

        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);

        log.info("Perfil de acesso atribuído: ROLE_USER.");
        emailService.sendEmail(usuario);
        log.info("Email de boas vindas enviado.");
        usuarioService.save(usuario);

        log.info("Usuário salvo na base de dados.");





        URI uri = uriComponentsBuilder.path("/cadastro/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));

    }

    @ApiOperation(value = "Deleta um usuário.", notes = "Para deletar um usuário, você precisa estar logado " +
            "no sistema. Nesse método só é possível deletar seu próprio usuário. Para testar, faça um cadastro antes, " +
            "pois precisará confirmar seu e-mail e senha para prosseguir.")

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestBody @Valid DeletarUsuarioDto deletarUsuarioDto, HttpServletRequest httpServletRequest){

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        log.info("Usuário encontrado a partir do token.");

        ResponseEntity<?> deletar = deletarUsuarioDto.deletar(emailUsuario, usuarioService);

        if(deletar.getStatusCode() == HttpStatus.BAD_REQUEST){
            log.info("Não foi possível prosseguir com a operação, pois os dados informados estão incorretos.");
            return new ResponseEntity<>("Dados inválidos.",HttpStatus.BAD_REQUEST);
        }

        log.info("Dados corretos, preparando para deletar usuário...");

        usuarioService.deleteUsuarioByEmail(emailUsuario);
        log.info("Usuário deletado.");
        return ResponseEntity.ok("Usuário deletado com sucesso.");

    }

    @ApiOperation(value = "Lista os usuários cadastrados.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir " +
            "um cadastro, copiar o token e colar na frente da palavra Bearer. Além disso, o usuário precisa ser um ADMIN, pois esse método" +
            " é restrito. Para isso, você pode cadastrar um usuário com o email \"@admin.com\"." +
            " Você também pode listar um usuário específico por e-mail, se preferir.")

    @GetMapping("/listar")
    public ResponseEntity<?> listar(@RequestParam(required = false) String email){

        if(email == null){
            log.info("Nenhum e-mail informado, retornando todos os usuários cadastrados.");
            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(UsuarioDto.converter(usuarios));
        }else {
            log.info("Procurando usuário na base de dados...");
            Usuario usuario = usuarioService.findUsuarioByEmail(email);
            if (usuario == null){
                log.info("E-mail não encontrado na base de dados.");
                return new ResponseEntity<>("Usuário não encontrado.",HttpStatus.NOT_FOUND);
            }
            log.info("Usuário encontrado. Retornando informações do usuário.");
            return ResponseEntity.ok(new UsuarioDto(usuario));

        }
    }

    @ApiOperation(value = "Atualiza o email do usuário.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir " +
            "um cadastro, copiar o token e colar na frente da palavra Bearer.")

   @PutMapping("atualizarEmail")
   public Object atualizarEmail(@RequestBody @Valid AtualizacaoUsuarioEmailDto atualizacaoUsuarioEmailDto, AtualizacaoUsuarioNomeDto nomeForm, HttpServletRequest httpServletRequest){

       String token = recuperarToken(httpServletRequest);
       String idUsuario = tokenService.getIdUsuario(token);
       String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        log.info("Usuário encontrado a partir do token.");

       ResponseEntity<?> atualizar = atualizacaoUsuarioEmailDto.atualizar(emailUsuario, usuarioService);

            if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
                log.info("Atualização não foi concluída.");
                return ResponseEntity.badRequest().body("E-mail deve ser diferente do anterior.");
            }

            log.info("E-mail atualizado.");
            return ResponseEntity.ok("E-mail atualizado com sucesso.");
   }

    @ApiOperation(value = "Atualiza o nome do usuário.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir " +
            "um cadastro, copiar o token e colar na frente da palavra Bearer.")

   @PutMapping("/atualizarNome")
   public ResponseEntity<?> atualizarNome(@RequestBody @Valid AtualizacaoUsuarioNomeDto atualizacaoUsuarioNomeDto, HttpServletRequest httpServletRequest){

       String token = recuperarToken(httpServletRequest);
       String idUsuario = tokenService.getIdUsuario(token);
       String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        log.info("Usuário encontrado a partir do token.");

       ResponseEntity<?> atualizar = atualizacaoUsuarioNomeDto.atualizar(emailUsuario, usuarioService);

       if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
           log.info("Atualização não foi concluída.");
           return ResponseEntity.badRequest().body("Nome deve ser diferente do anterior.");
       }
       log.info("Nome atualizado.");
       return ResponseEntity.ok("Nome atualizado com sucesso.");
   }

    @ApiOperation(value = "Atualiza a senha do usuário.", notes = "Esse método necessita de autenticação. Basta fazer o login, se já possuir " +
            "um cadastro, copiar o token e colar na frente da palavra Bearer.")

    @PutMapping("/atualizarSenha")
    public ResponseEntity<?> atualizarSenha(@RequestBody @Valid AtualizacaoSenhaDto atualizacaoSenhaDto, HttpServletRequest httpServletRequest){

        String token = recuperarToken(httpServletRequest);
        String idUsuario = tokenService.getIdUsuario(token);
        String emailUsuario= usuarioService.findById(idUsuario).get().getEmail();

        log.info("Usuário encontrado a partir do token.");

        ResponseEntity<?> atualizar = atualizacaoSenhaDto.atualizar(emailUsuario, usuarioService);

        if(atualizar.getStatusCode() == HttpStatus.BAD_REQUEST){
            log.info("Atualização não foi concluída.");
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
