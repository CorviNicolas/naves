package com.w2m.naves.service.impl;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import com.w2m.naves.model.entity.Nave;
import com.w2m.naves.model.repository.NavesRepository;
import com.w2m.naves.service.NaveService;
import io.restassured.RestAssured;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.specification.RequestSpecification;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;

import java.awt.print.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NavesServiceTests {

    @Autowired
    NaveService naveService;

    @MockBean
    NavesRepository mockNavesRepository;
    @MockBean
    KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void crearNave() {
        CrearNaveRequest request = new CrearNaveRequest("nombre");
        when(mockNavesRepository.save(any())).then(i -> {
            Nave nave = assertInstanceOf(Nave.class, i.getArgument(0));
            assertEquals("nombre", nave.getNombre());
            nave.setNaveId(1L);
            return nave;
        });
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(new CompletableFuture<>());
        NaveDTO result = naveService.crearNave(request);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.naveId()),
                () -> assertEquals("nombre", result.nombre())
        );
    }

    @Test
    void modificarNave() {
        ModificarNaveRequest request = new ModificarNaveRequest("nuevoNombre");
        Nave nave = new Nave();
        nave.setNombre("nombre");
        when(mockNavesRepository.findById(anyLong())).thenReturn(Optional.of(nave));
        NaveDTO result = naveService.modificarNave(1L, request);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("nuevoNombre", nave.getNombre())
        );
    }

    @Test
    void modificarNave_Falla_IdIncorrecto() {
        when(mockNavesRepository.findById(anyLong())).thenThrow(new NoSuchElementException());
        assertThrows(NoSuchElementException.class, () -> naveService.modificarNave(1L, new ModificarNaveRequest("nombre")));
    }

    @Test
    void eliminarNave() {
        Nave nave = new Nave();
        when(mockNavesRepository.findById(anyLong())).thenReturn(Optional.of(nave));
        doNothing().when(mockNavesRepository).delete(nave);
        naveService.eliminarNave(1L);
        verify(mockNavesRepository, times(1)).delete(nave);
    }


}
