package com.liberi.movies.controller;

import com.liberi.movies.dto.ApiMessageResponse;
import com.liberi.movies.model.DetalleOrden;
import com.liberi.movies.service.DetalleOrdenService;
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
@RequestMapping("/detalles-orden")
public class DetalleOrdenController {

    private final DetalleOrdenService detalleOrdenService;

    public DetalleOrdenController(DetalleOrdenService detalleOrdenService) {
        this.detalleOrdenService = detalleOrdenService;
    }

    @GetMapping
    public List<DetalleOrden> obtenerTodos() {
        return detalleOrdenService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public DetalleOrden obtenerPorId(@PathVariable Long id) {
        return detalleOrdenService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleOrden crear(@RequestBody DetalleOrden detalleOrden) {
        return detalleOrdenService.crear(detalleOrden);
    }

    @PutMapping("/{id}")
    public DetalleOrden actualizar(@PathVariable Long id, @RequestBody DetalleOrden detalleOrden) {
        return detalleOrdenService.actualizar(id, detalleOrden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> eliminar(@PathVariable Long id) {
        detalleOrdenService.eliminar(id);
        return ResponseEntity.ok(new ApiMessageResponse(HttpStatus.OK.value(), "Detalle de orden eliminado correctamente"));
    }
}
