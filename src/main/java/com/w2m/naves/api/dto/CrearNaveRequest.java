package com.w2m.naves.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearNaveRequest(@Schema(description = "El nombre de la nave")
                               @NotNull(message = "El nombre de la nave no puede estar vacío")
                               @NotBlank(message = "El nombre de la nave no puede estar vacío")
                               String nombre) {
}
