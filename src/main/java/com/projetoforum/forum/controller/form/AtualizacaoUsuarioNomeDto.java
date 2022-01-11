package com.projetoforum.forum.controller.form;

import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AtualizacaoUsuarioNomeDto {

    @NotBlank
    private String novoNome;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    public String getNovoNome() {
        return novoNome;
    }

    public void setNovoNome(String novoNome) {
        this.novoNome = novoNome;
    }

    public ResponseEntity<?> atualizar(String emailUsuario, UsuarioService usuarioService){
        Usuario usuario = usuarioService.findUsuarioByEmail(emailUsuario);
        String usuarioNome = usuario.getNome();

        log.info("Verificando nome informado...");

        if(!novoNome.matches(usuarioNome)){
            usuario.setNome(novoNome);
            usuarioService.save(usuario);
            log.info("Nome salvo na base de dados.");
            return ResponseEntity.ok().body(usuario);
        }else
            log.info("Ocorreu um erro: Nome não pode ser o mesmo.");
            return ResponseEntity.badRequest().build();
    }
}
