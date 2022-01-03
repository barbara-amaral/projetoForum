package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Topico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends MongoRepository<Topico, String> {
    Topico getById(String id);
    List<Topico> findTopicoByTag(String tag);
}
