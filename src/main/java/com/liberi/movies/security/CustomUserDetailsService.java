package com.liberi.movies.security;

import com.liberi.movies.model.Usuario;
import com.liberi.movies.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Esta clase se encarga de dado un username o mail, devolver el user con sus datos y roles 
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository; // Repositorio para acceder a los datos de los usuarios en la base de datos

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException { // Spring lo llama cuando necesita un user
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail) // Busca el user por username o email
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(usuario.getUsername()) // Crea un user de Spring con el username 
                .password(usuario.getPassword()) // con la contrasena 
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())) // y con el rol del usuario 
                .build();
    }
}
