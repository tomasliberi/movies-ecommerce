package com.liberi.movies.service;

import com.liberi.movies.model.Orden;
import com.liberi.movies.model.Usuario;
import com.liberi.movies.repository.OrdenRepository;
import com.liberi.movies.repository.UsuarioRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenService(OrdenRepository ordenRepository, UsuarioRepository usuarioRepository) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Orden> obtenerTodas() {
        return ordenRepository.findAll();
    }

    public Orden obtenerPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
    }

    public Orden crear(Orden orden) {
        orden.setId(null);
        prepararOrden(orden, orden);
        return guardar(orden);
    }

    public Orden actualizar(Long id, Orden ordenActualizada) {
        Orden ordenExistente = obtenerPorId(id);
        prepararOrden(ordenExistente, ordenActualizada);
        return guardar(ordenExistente);
    }

    public void eliminar(Long id) {
        Orden orden = obtenerPorId(id);
        try {
            ordenRepository.delete(orden);
            ordenRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la orden porque tiene detalles asociados"
            );
        }
    }

    private Orden guardar(Orden orden) {
        try {
            return ordenRepository.save(orden);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo guardar la orden por conflicto con los datos enviados"
            );
        }
    }

    private void prepararOrden(Orden destino, Orden origen) {
        destino.setUsuario(obtenerUsuario(origen));

        if (origen == null || origen.getFecha() == null || origen.getTotal() == null || origen.getEstado() == null
                || origen.getEstado().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha, total y estado son obligatorios");
        }

        destino.setFecha(origen.getFecha());
        destino.setTotal(origen.getTotal());
        destino.setEstado(origen.getEstado());
    }

    private Usuario obtenerUsuario(Orden orden) {
        if (orden == null || orden.getUsuario() == null || orden.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario de la orden es obligatorio");
        }

        return usuarioRepository.findById(orden.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
