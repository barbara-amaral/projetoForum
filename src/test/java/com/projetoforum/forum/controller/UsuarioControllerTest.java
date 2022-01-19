package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TopicoService topicoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mvc;

    @Autowired
    TokenService tokenService;


    @Test
    void cadastrar() throws Exception {
        URI uri = new URI("/cadastro");
        String json = "{\"nome\":\"Teste\",\"email\":\"teste@ibm.com\",\"senha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

        mongoTemplate.remove(usuarioService.findUsuarioByEmail("teste@ibm.com"));
    }

    @Test
    void naoCadastraCasoEmailEstejaEmBranco() throws Exception {
        URI uri = new URI("/cadastro");
        String json = "{\"nome\":\"Teste\",\"email\":\"\",\"senha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));

    }

    @Test
    @WithMockUser(username = "teste@ibm.com", password = "123456", roles = "ADMIN")
    void listar() throws Exception {
        URI uri = new URI("/cadastro/listar");
        mvc.
                perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser(username = "teste@ibm.com", password = "123456", roles = "USER")
    void naoListaCasoUsuarioNaoTenhaPerfilDeAdmin() throws Exception {
        URI uri = new URI("/cadastro/listar");
        mvc.
                perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(403));
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


        String json = "{\"email\":\"teste@testando.com\",\"senha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .delete("/cadastro/deletar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    void naoDeletaCasoEmailOuSenhaEstejamIncorretos() throws Exception {
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


        String json = "{\"email\":\"emailErrado@testando.com\",\"senha\":\"senhaErrada\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .delete("/cadastro/deletar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);

    }

    @Test
    void atualizarEmail() throws Exception {
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


        String json = "{\"novoEmail\":\"teste@atualizado.com\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarEmail")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);

    }

    @Test
    void naoAtualizadaCasoEmailSejaOMesmo() throws Exception {
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


        String json = "{\"novoEmail\":\"teste@testando.com\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarEmail")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);

    }

    @Test
    void atualizarNome() throws Exception {
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


        String json = "{\"novoNome\":\"Teste Atualizado\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarNome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);
    }

    @Test
    void naoAtualizaCasoNomeSejaOMesmo() throws Exception {
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


        String json = "{\"novoNome\":\"Teste\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarNome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);
    }

    @Test
    void atualizarSenha() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("senha"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("senha");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);


        String json = "{\"novaSenha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarSenha")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);
    }

    @Test
    void naoAtualizaCasoSenhaSejaAMesma() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("senha"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("senha");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);


        String json = "{\"novaSenha\":\"senha\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/cadastro/atualizarSenha")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);
    }
}