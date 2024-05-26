package com.w2m.naves.model.repository;

import com.w2m.naves.model.entity.Nave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NavesRepository extends JpaRepository<Nave, Long> {

    @Query("SELECT n FROM Nave n WHERE :nombre IS NULL OR UPPER(n.nombre) LIKE CONCAT('%',UPPER(:nombre), '%')")
    Page<Nave> obtenerNaves(@Param("nombre") String nombre, Pageable pageable);

}
