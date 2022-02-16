package com.projetoforum.forum.service;

import com.projetoforum.forum.model.EmailsUsuarios;
import com.projetoforum.forum.repository.EmailsUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailsUsuariosImpl implements EmailsUsuariosService{

    @Autowired
    EmailsUsuariosRepository emailsUsuariosRepository;


    @Override
    public EmailsUsuarios save(EmailsUsuarios emailsUsuarios) {
        return emailsUsuariosRepository.save(emailsUsuarios);
    }

    @Override
    public List<EmailsUsuarios> findAll() {
        return emailsUsuariosRepository.findAll();
    }

    @Override
    public void deleteByEmail(String email) {
        emailsUsuariosRepository.deleteByEmail(email);
    }

    @Override
    public EmailsUsuarios findEmailsUsuariosByEmail(String email) {
        return emailsUsuariosRepository.findEmailsUsuariosByEmail(email);
    }
}
