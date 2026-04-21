package com.liberi.movies.controller;

import com.liberi.movies.dto.ApiMessageResponse;
import com.liberi.movies.model.Orden;
import com.liberi.movies.service.OrdenService;
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
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public List<Orden> obtenerTodas() {
        return ordenService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Orden obtenerPorId(@PathVariable Long id) {
        return ordenService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Orden crear(@RequestBody Orden orden) {
        return ordenService.crear(orden);
    }

    @PutMapping("/{id}")
    public Orden actualizar(@PathVariable Long id, @RequestBody Orden orden) {
        return ordenService.actualizar(id, orden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> eliminar(@PathVariable Long id) {
        ordenService.eliminar(id);
        return ResponseEntity.ok(new ApiMessageResponse(HttpStatus.OK.value(), "Orden eliminada correctamente"));
    }
}
