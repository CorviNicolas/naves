package com.w2m.naves.service;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NaveService {

    Page<NaveDTO> obtenerNaves(String nombre, Pageable pageable);
    NaveDTO obtenerNave(Long id);
    NaveDTO crearNave(CrearNaveRequest crearNave);
    NaveDTO modificarNave(Long id, ModificarNaveRequest modificarNave);
    void eliminarNave(Long id);
}
