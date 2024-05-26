package com.w2m.naves.api.dto;

import com.w2m.naves.model.entity.Nave;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public record NaveDTO(@Schema(description = "El id de la nave") Long naveId, @Schema(description = "El nombre de la nave") String nombre) {

    public static NaveDTO fromEntity(Nave nave) {
        return new NaveDTO(nave.getNaveId(), nave.getNombre());
    }
}
