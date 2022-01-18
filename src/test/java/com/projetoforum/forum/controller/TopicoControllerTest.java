package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.AutenticacaoService;
import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;

@SpringBootTest
@AutoConfigureMockMvc
class TopicoControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private MongoTemplate mongoTemplate;

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
    @WithUserDetails(value = "valeria@ibm.com")
    void cadastrar() throws Exception {

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("valeria@ibm.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);
        System.out.println(token);

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
    }

    @Test
    void deletar() throws Exception {

        Usuario usuario = usuarioService.findUsuarioByEmail("valeria@ibm.com");
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("valeria@ibm.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);
        System.out.println(token);

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
    }

    @Test
    void atualizar() {

    }

    @Test
    void listar() {
    }
}