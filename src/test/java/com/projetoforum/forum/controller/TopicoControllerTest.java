package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TopicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    TopicoService topicoService;

    LoginDto loginDto;

    Authentication authentication;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Test
    void cadastraNovoTopico() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        URI uri = new URI("/topicos/novotopico");
        String json = "{\"titulo\":\"Teste\",\"mensagem\":\"Testando\",\"tag\":\"teste\"}";

            mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization","Bearer "+token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

            List <Topico> topicos =  topicoService.findTopicoByTag("teste");
            Topico topico = topicos.get(0);
            mongoTemplate.remove(topico);
            mongoTemplate.remove(usuario);
    }

    @Test
    void naoCadastraNovoTopicoSeEstiverSemTitulo() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        URI uri = new URI("/topicos/novotopico");
        String json = "{\"titulo\":\"\",\"mensagem\":\"Testando\",\"tag\":\"teste\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization","Bearer "+token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);
    }

    @Test
    void deletar() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        String id = topico.getId();

        mvc
                .perform(MockMvcRequestBuilders
                        .delete("/topicos/{id}",id)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);
        mongoTemplate.remove(topico);
    }

    @Test
    void naoDeletaCasoNaoSejaAutorDoTopico() throws Exception{
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Usuario autor = new Usuario();
        usuario.setNome("Teste 2");
        usuario.setEmail("teste2@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil2 = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(autor);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(autor);
        topicoService.save(topico);

        String id = topico.getId();

        mvc
                .perform(MockMvcRequestBuilders
                        .delete("/topicos/{id}",id)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);
        mongoTemplate.remove(topico);
        mongoTemplate.remove(autor);
    }

    @Test
    void atualizar() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        String id = topico.getId();

        String json = "{\"mensagem\":\"Atualizando topico\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/topicos/{id}",id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);
    }

    @Test
    void naoAtualizaCasoMensagemEstejaVazia() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        String id = topico.getId();

        String json = "{\"mensagem\":\"\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/topicos/{id}",id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);
    }

    @Test
    void listarTodos() throws Exception {
        URI uri = new URI("/topicos/listar");

        mvc.
                perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void listarPorTag() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        String tag = topico.getTag();

        mvc.
                perform(MockMvcRequestBuilders.get("/topicos/listar")
                        .param("tag", tag))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);
        mongoTemplate.remove(topico);
    }

    @Test
    void naoListaCasoNaoEncontreTopicosComATagInformadaERetornaMensagemDeErro() throws Exception {

        String tag = "nao existe";

        MvcResult result = mvc.
                perform(MockMvcRequestBuilders.get("/topicos/listar")
                        .param("tag", tag))
                .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

        String mensagemDeErro = result.getResponse().getContentAsString();
        Assert.assertEquals(mensagemDeErro, "Não existem tópicos cadastrados com a tag "+tag+".");
    }
}