package com.example.ActHos.ActividadHospital_19_05.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;

    @Column(unique = false, length = 50, nullable = false)
    private String nombre;

    @Column(unique = false, length = 200, nullable = false)
    private String descripcion;
}
