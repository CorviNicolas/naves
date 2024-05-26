package com.w2m.naves.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CrearNaveRequest(@Schema(description = "El nombre de la nave") String nombre) {
}
