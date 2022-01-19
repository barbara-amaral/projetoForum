package com.projetoforum.forum.controller;

import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void autenticar() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        URI uri = new URI("/auth");
        String json = "{\"email\":\"teste@testando.com\",\"senha\":\"123456\"}";

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuario);
    }

    @Test
    public void naoAutenticaCasoEmailOuSenhaEstejamErrados() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        URI uri = new URI("/auth");
        String json = "{\"email\":\"emailErrado@ibm.com\",\"senha\":\"senhaErrada\"}";

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));

        mongoTemplate.remove(usuario);

    }
}