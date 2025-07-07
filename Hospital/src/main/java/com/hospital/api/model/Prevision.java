package com.hospital.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Prevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrevision;

    @Column(unique = false, length = 50, nullable = false)
    private String nombre;

    @Column(unique = false, length = 200, nullable = false)
    private Integer cobertura;
}
