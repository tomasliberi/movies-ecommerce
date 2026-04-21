package com.liberi.movies.service;

import com.liberi.movies.model.Carrito;
import com.liberi.movies.model.ItemCarrito;
import com.liberi.movies.model.Producto;
import com.liberi.movies.repository.CarritoRepository;
import com.liberi.movies.repository.ItemCarritoRepository;
import com.liberi.movies.repository.ProductoRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ItemCarritoService {

    private final ItemCarritoRepository itemCarritoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public ItemCarritoService(ItemCarritoRepository itemCarritoRepository, CarritoRepository carritoRepository,
                              ProductoRepository productoRepository) {
        this.itemCarritoRepository = itemCarritoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    public List<ItemCarrito> obtenerTodos() {
        return itemCarritoRepository.findAll();
    }

    public ItemCarrito obtenerPorId(Long id) {
        return itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de carrito no encontrado"));
    }

    public ItemCarrito crear(ItemCarrito itemCarrito) {
        itemCarrito.setId(null);
        asignarRelaciones(itemCarrito, itemCarrito);
        return guardar(itemCarrito);
    }

    public ItemCarrito actualizar(Long id, ItemCarrito itemCarritoActualizado) {
        ItemCarrito itemExistente = obtenerPorId(id);
        asignarRelaciones(itemExistente, itemCarritoActualizado);
        itemExistente.setCantidad(itemCarritoActualizado.getCantidad());
        return guardar(itemExistente);
    }

    public void eliminar(Long id) {
        ItemCarrito itemCarrito = obtenerPorId(id);
        itemCarritoRepository.delete(itemCarrito);
    }

    private ItemCarrito guardar(ItemCarrito itemCarrito) {
        try {
            return itemCarritoRepository.save(itemCarrito);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo guardar el item porque ese producto ya existe en el carrito"
            );
        }
    }

    private void asignarRelaciones(ItemCarrito destino, ItemCarrito origen) {
        destino.setCarrito(obtenerCarrito(origen));
        destino.setProducto(obtenerProducto(origen));

        if (origen == null || origen.getCantidad() == null || origen.getCantidad() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }

        destino.setCantidad(origen.getCantidad());
    }

    private Carrito obtenerCarrito(ItemCarrito itemCarrito) {
        if (itemCarrito == null || itemCarrito.getCarrito() == null || itemCarrito.getCarrito().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito es obligatorio");
        }

        return carritoRepository.findById(itemCarrito.getCarrito().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
    }

    private Producto obtenerProducto(ItemCarrito itemCarrito) {
        if (itemCarrito == null || itemCarrito.getProducto() == null || itemCarrito.getProducto().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto es obligatorio");
        }

        return productoRepository.findById(itemCarrito.getProducto().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
}
