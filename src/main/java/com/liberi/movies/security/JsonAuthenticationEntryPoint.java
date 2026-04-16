package com.liberi.movies.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Establece el codigo de estado http
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Establece el tipo de contenido de la respuesta para devolver json
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // Asegura que los caracteres especiales se vean bien
        response.getWriter().write("""
                {"status":401,"mensaje":"No autenticado o token invalido"} // Escribe un mensaje JSON en la respuesta indicando que no se ha autenticado o el token es invalido
                """);
    }
}
