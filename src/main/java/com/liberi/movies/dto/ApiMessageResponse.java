package com.liberi.movies.dto;

public class ApiMessageResponse {

    private final int status;
    private final String mensaje;

    public ApiMessageResponse(int status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }
}
