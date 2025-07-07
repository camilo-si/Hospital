package com.hospital.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Paciente{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    @Column(unique = true, length = 12, nullable = false)
    private String run;

    @Column(unique = false, length = 50, nullable = false)
    private String nombre;

    @Column(unique = false, length = 50, nullable = false)
    private String apellido;

    @Column(unique = false, nullable = false)
    private LocalDate fechaNacimiento;

    @Column(unique = true, length = 100, nullable = false)
    private String correo;

    @Column(unique = false, length = 20, nullable = false)
    private String telefono;


    // TABLA PIVOTE PREVESION
    @ManyToOne
    @JoinColumn(name = "id_prevision", nullable = false)
    private Prevision prevision;




}