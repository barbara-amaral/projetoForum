package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario,String> {
    Usuario findUsuarioByEmail(String email);
}
