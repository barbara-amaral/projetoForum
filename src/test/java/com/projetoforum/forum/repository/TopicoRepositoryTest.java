package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class TopicoRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TopicoRepository topicoRepository;

    @Test
    void getById() {
        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topicoRepository.save(topico);
        String id = topico.getId();

        topico = topicoRepository.getById(id);
        Assert.assertNotNull(topico);
        Assert.assertEquals(id, topico.getId());

        mongoTemplate.remove(topico);
    }

    @Test
    void findTopicoByTag() {
        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topicoRepository.save(topico);
        String tag = topico.getTag();

        List<Topico> topicos = topicoRepository.findTopicoByTag(tag);
        topico = topicos.get(0);
        Assert.assertNotNull(topico);
        Assert.assertEquals(tag, topico.getTag());

        mongoTemplate.remove(topico);
    }
}