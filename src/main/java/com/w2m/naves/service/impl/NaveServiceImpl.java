package com.w2m.naves.service.impl;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import com.w2m.naves.model.entity.Nave;
import com.w2m.naves.model.repository.NavesRepository;
import com.w2m.naves.service.NaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class NaveServiceImpl implements NaveService {

    private final NavesRepository navesRepository;

    public NaveServiceImpl(NavesRepository navesRepository) {
        this.navesRepository = navesRepository;
    }

    @Override
    public Page<NaveDTO> obtenerNaves(String nombre, Pageable pageable) {
        return navesRepository.obtenerNaves(nombre, pageable).map(NaveDTO::fromEntity);
    }

    @Override
    public NaveDTO obtenerNave(Long id) {
        return NaveDTO.fromEntity(navesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Nave con id "
                + id + " no encontrada")));
    }

    @Override
    public NaveDTO crearNave(CrearNaveRequest crearNave) {
        return NaveDTO.fromEntity(navesRepository.save(new Nave(crearNave)));
    }

    @Override
    public NaveDTO modificarNaveRequest(Long id, ModificarNaveRequest modificarNave) {
        Nave nave = navesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Nave con id "
                + id + " no encontrada"));
        nave.modificarNave(modificarNave);
        return NaveDTO.fromEntity(nave);
    }

    @Override
    public void eliminarNave(Long id) {
        Nave nave = navesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Nave con id "
                + id + " no encontrada"));
        navesRepository.delete(nave);
    }
}
