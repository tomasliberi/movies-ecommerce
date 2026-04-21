package com.liberi.movies.service;

import com.liberi.movies.model.Carrito;
import com.liberi.movies.model.Usuario;
import com.liberi.movies.repository.CarritoRepository;
import com.liberi.movies.repository.UsuarioRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository, UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Carrito> obtenerTodos() {
        return carritoRepository.findAll();
    }

    public Carrito obtenerPorId(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
    }

    public Carrito crear(Carrito carrito) {
        carrito.setId(null);
        carrito.setUsuario(obtenerUsuario(carrito));
        return guardar(carrito);
    }

    public Carrito actualizar(Long id, Carrito carritoActualizado) {
        Carrito carritoExistente = obtenerPorId(id);
        carritoExistente.setUsuario(obtenerUsuario(carritoActualizado));
        return guardar(carritoExistente);
    }

    public void eliminar(Long id) {
        Carrito carrito = obtenerPorId(id);
        try {
            carritoRepository.delete(carrito);
            carritoRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el carrito porque esta asociado a uno o mas items"
            );
        }
    }

    private Carrito guardar(Carrito carrito) {
        try {
            return carritoRepository.save(carrito);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo guardar el carrito porque el usuario ya tiene uno asociado"
            );
        }
    }

    private Usuario obtenerUsuario(Carrito carrito) {
        if (carrito == null || carrito.getUsuario() == null || carrito.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario del carrito es obligatorio");
        }

        return usuarioRepository.findById(carrito.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
