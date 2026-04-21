package com.liberi.movies.controller;

import com.liberi.movies.dto.ApiMessageResponse;
import com.liberi.movies.model.ItemCarrito;
import com.liberi.movies.service.ItemCarritoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items-carrito")
public class ItemCarritoController {

    private final ItemCarritoService itemCarritoService;

    public ItemCarritoController(ItemCarritoService itemCarritoService) {
        this.itemCarritoService = itemCarritoService;
    }

    @GetMapping
    public List<ItemCarrito> obtenerTodos() {
        return itemCarritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ItemCarrito obtenerPorId(@PathVariable Long id) {
        return itemCarritoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemCarrito crear(@RequestBody ItemCarrito itemCarrito) {
        return itemCarritoService.crear(itemCarrito);
    }

    @PutMapping("/{id}")
    public ItemCarrito actualizar(@PathVariable Long id, @RequestBody ItemCarrito itemCarrito) {
        return itemCarritoService.actualizar(id, itemCarrito);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> eliminar(@PathVariable Long id) {
        itemCarritoService.eliminar(id);
        return ResponseEntity.ok(new ApiMessageResponse(HttpStatus.OK.value(), "Item de carrito eliminado correctamente"));
    }
}
