package com.liberi.movies.service;

import com.liberi.movies.model.DetalleOrden;
import com.liberi.movies.model.Orden;
import com.liberi.movies.model.Producto;
import com.liberi.movies.repository.DetalleOrdenRepository;
import com.liberi.movies.repository.OrdenRepository;
import com.liberi.movies.repository.ProductoRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DetalleOrdenService {

    private final DetalleOrdenRepository detalleOrdenRepository;
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;

    public DetalleOrdenService(DetalleOrdenRepository detalleOrdenRepository, OrdenRepository ordenRepository,
                               ProductoRepository productoRepository) {
        this.detalleOrdenRepository = detalleOrdenRepository;
        this.ordenRepository = ordenRepository;
        this.productoRepository = productoRepository;
    }

    public List<DetalleOrden> obtenerTodos() {
        return detalleOrdenRepository.findAll();
    }

    public DetalleOrden obtenerPorId(Long id) {
        return detalleOrdenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de orden no encontrado"));
    }

    public DetalleOrden crear(DetalleOrden detalleOrden) {
        detalleOrden.setId(null);
        asignarRelaciones(detalleOrden, detalleOrden);
        return guardar(detalleOrden);
    }

    public DetalleOrden actualizar(Long id, DetalleOrden detalleOrdenActualizado) {
        DetalleOrden detalleExistente = obtenerPorId(id);
        asignarRelaciones(detalleExistente, detalleOrdenActualizado);
        return guardar(detalleExistente);
    }

    public void eliminar(Long id) {
        DetalleOrden detalleOrden = obtenerPorId(id);
        detalleOrdenRepository.delete(detalleOrden);
    }

    private DetalleOrden guardar(DetalleOrden detalleOrden) {
        try {
            return detalleOrdenRepository.save(detalleOrden);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo guardar el detalle porque ese producto ya existe en la orden"
            );
        }
    }

    private void asignarRelaciones(DetalleOrden destino, DetalleOrden origen) {
        destino.setOrden(obtenerOrden(origen));
        destino.setProducto(obtenerProducto(origen));

        if (origen == null || origen.getCantidad() == null || origen.getCantidad() <= 0
                || origen.getPrecioUnitario() == null || origen.getPrecioUnitario() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cantidad y precio unitario validos son obligatorios"
            );
        }

        destino.setCantidad(origen.getCantidad());
        destino.setPrecioUnitario(origen.getPrecioUnitario());
    }

    private Orden obtenerOrden(DetalleOrden detalleOrden) {
        if (detalleOrden == null || detalleOrden.getOrden() == null || detalleOrden.getOrden().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La orden es obligatoria");
        }

        return ordenRepository.findById(detalleOrden.getOrden().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
    }

    private Producto obtenerProducto(DetalleOrden detalleOrden) {
        if (detalleOrden == null || detalleOrden.getProducto() == null || detalleOrden.getProducto().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto es obligatorio");
        }

        return productoRepository.findById(detalleOrden.getProducto().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
}
