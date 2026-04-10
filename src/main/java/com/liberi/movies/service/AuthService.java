package com.liberi.movies.service;

import com.liberi.movies.dto.AuthLoginRequest;
import com.liberi.movies.dto.AuthRegisterRequest;
import com.liberi.movies.dto.AuthResponse;
import com.liberi.movies.model.RolUsuario;
import com.liberi.movies.model.Usuario;
import com.liberi.movies.repository.UsuarioRepository;
import com.liberi.movies.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new AuthResponse(
                jwtService.generarToken(user),
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol().name()
        );
    }

    public AuthResponse register(AuthRegisterRequest request) {
        validarRegistro(request);

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol() != null ? request.getRol() : RolUsuario.COMPRADOR);
        usuario.setDireccion(request.getDireccion());

        Usuario guardado = usuarioRepository.save(usuario);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(guardado.getUsername())
                .password(guardado.getPassword())
                .authorities("ROLE_" + guardado.getRol().name())
                .build();

        return new AuthResponse(
                jwtService.generarToken(userDetails),
                guardado.getId(),
                guardado.getUsername(),
                guardado.getRol().name()
        );
    }

    private void validarRegistro(AuthRegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getNombre() == null || request.getNombre().isBlank()
                || request.getApellido() == null || request.getApellido().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios para registrar el usuario");
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya esta en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
    }
}
