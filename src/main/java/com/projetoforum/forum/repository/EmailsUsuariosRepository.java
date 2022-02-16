package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.EmailsUsuarios;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailsUsuariosRepository extends MongoRepository<EmailsUsuarios, String> {
    void deleteByEmail(String email);
    EmailsUsuarios findEmailsUsuariosByEmail(String email);
}
