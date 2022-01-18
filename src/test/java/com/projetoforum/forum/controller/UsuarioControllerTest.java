package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    TokenService tokenService;


    @Test
    void cadastrar() throws Exception {
        URI uri = new URI("/cadastro");
        String json = "{\"nome\":\"Lucas\",\"email\":\"lucasteste@ibm.com\",\"senha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

        mongoTemplate.remove(usuarioService.findUsuarioByEmail("lucasteste@ibm.com"));
    }

    @Test
    @WithMockUser(username = "mia@ibm.com", password = "123456", roles = "ADMIN")
    void listar() throws Exception {
        URI uri = new URI("/cadastro/listar");
        mvc.
                perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void deletar() {

    }

    @Test
    void atualizarEmail(){

    }

    @Test
    void atualizarNome() {
    }

    @Test
    void atualizarSenha() {
    }
}