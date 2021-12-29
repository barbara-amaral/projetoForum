package com.projetoforum.forum.service;

import com.projetoforum.forum.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UsuarioService {
    Usuario findUsuarioByEmail(String email);
    Optional<Usuario> findById(String idUsuario);
    Usuario save(Usuario usuario);
}
