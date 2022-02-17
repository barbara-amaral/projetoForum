package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.EmailsUsuarios;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailsUsuariosRepository extends MongoRepository<EmailsUsuarios, String> {
    void deleteByEmail(String email);
    EmailsUsuarios findEmailsUsuariosByEmail(String email);
    Optional<EmailsUsuarios> findByEmail(String email);
}
