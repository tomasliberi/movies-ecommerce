package com.liberi.movies.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component // Esta clase lo que hace es verificar en cada request si el token es valido, si lo es, lo deja pasar
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Filtro que se ejecuta una vez por cada solicitud para validar el token JWT

    private final JwtService jwtService; // Variable con el JWTService
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) // Recibe , la solicitud, la respuesta y el filtro de cadena para continuar con el proceso de filtrado
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtiene el encabezado de autorización de la solicitud, que se espera que contenga el token JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Si el encabezado es nulo o no comienza con "Bearer " no lo procesa
            filterChain.doFilter(request, response); // Deja pasar la solicitud al siguiente filtro sin hacer nada
            return;
        }

        String token = authHeader.substring(7); // Saca el bearer y se queda solo con el token para procesarlo
        String username = jwtService.extraerUsername(token); // Extrae el username del token usando el JWTService 

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Revisa que no sea nulo y que no haya sido autenticado 
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.esTokenValido(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
