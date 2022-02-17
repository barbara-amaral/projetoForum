package com.projetoforum.forum.service;

import com.projetoforum.forum.model.EmailsUsuarios;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmailsUsuariosService {
    EmailsUsuarios save(EmailsUsuarios emailsUsuarios);
    List<EmailsUsuarios> findAll();
    void deleteByEmail(String email);
    EmailsUsuarios findEmailsUsuariosByEmail(String email);
    Optional<EmailsUsuarios> findByEmail(String email);
}
