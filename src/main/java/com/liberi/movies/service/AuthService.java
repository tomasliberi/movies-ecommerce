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

    private final AuthenticationManager authenticationManager; // Autenticador de user y password
    private final JwtService jwtService; // Genera tokens JWT para los usuarios autenticados
    private final UsuarioRepository usuarioRepository; // BaseDeDatos
    private final PasswordEncoder passwordEncoder; // Para hashear la contrasena 

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthLoginRequest request) { // Recibe request con username o email y password y devuelve el response con el token 
        Authentication authentication = authenticationManager.authenticate( 
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()) // Crea un objeto con username y password y se lo pasa a Spring para que lo autentique
        );

        UserDetails user = (UserDetails) authentication.getPrincipal(); // Si encotnro el user lo guarda en una variable 
        Usuario usuario = usuarioRepository.findByUsername(user.getUsername()) // Busca el usuario en la base de datos para obtener su id y rol, usando el username que extrajo del token
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new AuthResponse(
                jwtService.generarToken(user),
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol().name()
        ); // Genera un token para el user autenticado y devuelve el response con el token, id, username y rol del usuario
    }

    public AuthResponse register(AuthRegisterRequest request) { // Recibe la request con los datos y devuelve un token 
        validarRegistro(request); // Valida los datos 

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // Hashea la contrasena antes de guardarla en la base de datos
        usuario.setRol(request.getRol() != null ? request.getRol() : RolUsuario.COMPRADOR); //Si en la request no esta el rol, le asigna comprador
        usuario.setDireccion(request.getDireccion());

        Usuario guardado = usuarioRepository.save(usuario); //LO guarda en la BD
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(guardado.getUsername())
                .password(guardado.getPassword()) // Creamos un UserDetails poruqe el jWTService necesita un userDetails para generar el token
                .authorities("ROLE_" + guardado.getRol().name())
                .build();

        return new AuthResponse(
                jwtService.generarToken(userDetails),
                guardado.getId(),
                guardado.getUsername(),
                guardado.getRol().name()
        );
    }

    private void validarRegistro(AuthRegisterRequest request) { // Para validar antes dde registrar 
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getNombre() == null || request.getNombre().isBlank()
                || request.getApellido() == null || request.getApellido().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios para registrar el usuario");
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) { //Verifica que no se repita el userename 
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya esta en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) { // Verifica qe no se repita el mail
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
    }
}
