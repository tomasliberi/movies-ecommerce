package com.liberi.movies.service;

import com.liberi.movies.model.Usuario;
import com.liberi.movies.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        validarDuplicados(usuario, null);
        usuario.setId(null);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = obtenerPorId(id);
        validarDuplicados(usuarioActualizado, id);
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }
        usuarioExistente.setRol(usuarioActualizado.getRol());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    private void validarDuplicados(Usuario usuario, Long usuarioIdActual) {
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del usuario son obligatorios");
        }

        usuarioRepository.findByUsername(usuario.getUsername())
                .filter(encontrado -> !encontrado.getId().equals(usuarioIdActual))
                .ifPresent(encontrado -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya esta en uso");
                });

        usuarioRepository.findByEmail(usuario.getEmail())
                .filter(encontrado -> !encontrado.getId().equals(usuarioIdActual))
                .ifPresent(encontrado -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
                });
    }
}
