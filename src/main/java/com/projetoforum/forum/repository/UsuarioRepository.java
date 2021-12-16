package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario,String> {
}
