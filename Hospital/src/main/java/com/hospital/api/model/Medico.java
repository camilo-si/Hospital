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
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedico;

    @Column(unique = true, length = 12, nullable = false)
    private String run ;

    @Column(unique = false, length = 50, nullable = false)
    private String nombre ;

    @Column(unique = false, length = 50, nullable = false)
    private String apellido ;

    @Column(nullable = false)
    private LocalDate fechaContrato ;

    @Column(nullable = false)
    private Float sueldo_base ;

    @Column(unique = true, length = 100, nullable = false)
    private String correo ;

    @Column(unique = true, length = 20, nullable = false)
    private String telefono ;

    //OBJETO MTONE
    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad ;
}
