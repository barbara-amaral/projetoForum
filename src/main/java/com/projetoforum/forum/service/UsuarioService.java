package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsuarioService {
    Usuario findUsuarioByEmail(String email);
    Optional<Usuario> findById(String idUsuario);
    Usuario save(Usuario usuario);
    void deleteUsuarioByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
}
