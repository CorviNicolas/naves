package com.w2m.naves.service.impl;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.NaveDTO;
import com.w2m.naves.model.entity.Nave;
import com.w2m.naves.model.repository.NavesRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NavesServiceIntegrationTests {

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @Autowired
    NavesRepository navesRepository;

    @Test
    void consultarNave() {
        Nave nave = new Nave();
        nave.setNaveId(1L);
        nave.setNombre("nombre");
        navesRepository.save(nave);
        Nave response =
                RestAssured.with()
                        .auth().basic("user", "user")
                        .when()
                        .get(uri + "/naves/1")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .as(Nave.class);

        assertAll(
                () -> assertEquals(1L, response.getNaveId()),
                () -> assertEquals("nombre", response.getNombre())
        );
    }

    @Test
    void consultarNaves() {
        Nave nave1 = new Nave();
        nave1.setNaveId(1L);
        nave1.setNombre("nombre");

        Nave nave2 = new Nave();
        nave2.setNaveId(2L);
        nave2.setNombre("nombre2");

        Nave nave3 = new Nave();
        nave3.setNaveId(3L);
        nave3.setNombre("nave");

        navesRepository.saveAll(List.of(nave1, nave2, nave3));

        HashMap<String, Object> result = RestAssured.with()
                .auth().basic("user", "user")
                .when()
                .get(uri + "/naves")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(HashMap.class);


        HashMap<String, Object> resultConFiltro = RestAssured.with()
                .queryParam("nombre", "nombre")
                .auth().basic("user", "user")
                .when()
                .get(uri + "/naves")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(HashMap.class);

        assertEquals(3, result.get("totalElements"));
        assertEquals(2, resultConFiltro.get("totalElements"));
    }

    @Test
    void crearNave_Falla_NombreNull(){
        RestAssured.with()
                .auth().basic("user", "user")
                .contentType(ContentType.JSON)
                .body(new CrearNaveRequest(""))
                .when()
                .post(uri + "/naves")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
