package com.w2m.naves.model.entity;

import com.w2m.naves.api.dto.CrearNaveRequest;
import com.w2m.naves.api.dto.ModificarNaveRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "idx_nave_nombre", columnList = "nombre"))
public class Nave {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long naveId;
    @Column(nullable = false)
    private String nombre;

    public Long getNaveId() {
        return naveId;
    }

    public Nave() {
    }

    public Nave(CrearNaveRequest crearNave) {
        this.nombre = crearNave.nombre();
    }

    public void setNaveId(Long naveId) {
        this.naveId = naveId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void modificarNave(ModificarNaveRequest modificarNave) {
        if(modificarNave.nombre() != null) {
            this.nombre = modificarNave.nombre();
        }
    }
}
