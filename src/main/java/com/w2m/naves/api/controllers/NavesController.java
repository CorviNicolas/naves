package com.w2m.naves.api.controllers;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import com.w2m.naves.service.NaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/naves")
@RestController
public class NavesController {

    private final NaveService naveService;

    public NavesController(NaveService naveService) {
        this.naveService = naveService;
    }

    @Operation(description = "Obtiene una pagina de naves filtrando opcionalmente por nombre")
    @GetMapping
    public Page<NaveDTO> obtenerNaves(
            @Schema(description = "El nombre de la nave") @RequestParam(value = "nombre", required = false) String nombre,
            Pageable pageable) {
        return naveService.obtenerNaves(nombre, pageable);
    }

    @Operation(description = "Obtiene una nave por id")
    @GetMapping("/{id}")
    public NaveDTO obtenerNave(@PathVariable Long id) {
        return naveService.obtenerNave(id);
    }

    @Operation(description = "Modifica una nave")
    @PostMapping
    public NaveDTO crearNave(@RequestBody CrearNaveRequest crearNave) {
        return naveService.crearNave(crearNave);
    }

    @Operation(description = "Modifica una nave")
    @PutMapping("/{id}")
    public NaveDTO modificarNave(@PathVariable Long id, @RequestBody ModificarNaveRequest modificarNave) {
        return naveService.modificarNaveRequest(id, modificarNave);
    }

    @Operation(description = "Elimina una nave")
    @DeleteMapping("/{id}")
    public void eliminarNave(@PathVariable Long id) {
        naveService.eliminarNave(id);
    }

}
