package com.liberi.movies.repository;

import com.liberi.movies.model.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("""
            SELECT p
            FROM Producto p
            WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
              AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
              AND (:precioMin IS NULL OR p.precio >= :precioMin)
              AND (:precioMax IS NULL OR p.precio <= :precioMax)
              AND (:soloDisponibles = FALSE OR p.stock > 0)
            """)
    List<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("categoriaId") Long categoriaId,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax,
            @Param("soloDisponibles") boolean soloDisponibles
    );
}
