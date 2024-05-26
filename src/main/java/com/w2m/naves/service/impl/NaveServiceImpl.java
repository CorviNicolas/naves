package com.w2m.naves.service.impl;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import com.w2m.naves.model.entity.Nave;
import com.w2m.naves.model.repository.NavesRepository;
import com.w2m.naves.service.NaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class NaveServiceImpl implements NaveService {

    private static final Logger LOG = LoggerFactory.getLogger(NaveServiceImpl.class);
    private final NavesRepository navesRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public NaveServiceImpl(NavesRepository navesRepository,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.navesRepository = navesRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Page<NaveDTO> obtenerNaves(String nombre, Pageable pageable) {
        return navesRepository.obtenerNaves(nombre, pageable).map(NaveDTO::fromEntity);
    }

    @Override
    @Cacheable("nave")
    public NaveDTO obtenerNave(Long id) {
        return NaveDTO.fromEntity(navesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Nave con id "
                + id + " no encontrada")));
    }

    @Override
    public NaveDTO crearNave(CrearNaveRequest crearNave) {
        Nave nave = new Nave(crearNave);
        navesRepository.save(nave);
        try {
            kafkaTemplate.send("naves-topic", nave.getNombre());
        } catch (Exception e) {
            LOG.warn("No se pudo emitir un evento de creación de nave", e);
        }
        return NaveDTO.fromEntity(nave);
    }

    @Override
    public NaveDTO modificarNave(Long id, ModificarNaveRequest modificarNave) {
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

    @KafkaListener(topics = "naves-topic")
    public void handleEvent(String nombreNave) {
        LOG.info("Dentro del listener de Kafka. Se ha creado una nueva nave con nombre: {}", nombreNave);
    }
}
