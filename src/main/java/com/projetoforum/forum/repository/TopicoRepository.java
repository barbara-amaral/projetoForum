package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Topico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends MongoRepository<Topico, String> {
}
