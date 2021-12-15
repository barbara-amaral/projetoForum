package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Topico;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicoRepository extends MongoRepository<Topico, String> {
}
