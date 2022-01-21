package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Resposta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespostaRepository extends MongoRepository<Resposta, String> {

}
