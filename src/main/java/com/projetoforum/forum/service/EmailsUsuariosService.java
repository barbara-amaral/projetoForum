package com.projetoforum.forum.service;

import com.projetoforum.forum.model.EmailsUsuarios;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailsUsuariosService {
    EmailsUsuarios save(EmailsUsuarios emailsUsuarios);
    List<EmailsUsuarios> findAll();
    void deleteByEmail(String email);
    EmailsUsuarios findEmailsUsuariosByEmail(String email);
}
