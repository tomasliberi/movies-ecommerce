package com.liberi.movies.dto;

public class AuthResponse {

    private final String token;
    private final String tipo;
    private final Long usuarioId;
    private final String username;
    private final String rol;

    public AuthResponse(String token, Long usuarioId, String username, String rol) {
        this.token = token;
        this.tipo = "Bearer";
        this.usuarioId = usuarioId;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }
}
