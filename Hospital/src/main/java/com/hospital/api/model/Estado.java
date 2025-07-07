package com.hospital.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstado;

    @Column(unique = false, length = 50, nullable = false)
    private String nombre;

    @Column(unique = false, length = 200, nullable = false)
    private String descripcion;
}
