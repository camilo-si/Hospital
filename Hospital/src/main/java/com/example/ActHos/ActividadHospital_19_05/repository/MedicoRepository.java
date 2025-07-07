package com.example.ActHos.ActividadHospital_19_05.repository;

import com.example.ActHos.ActividadHospital_19_05.model.Medico;
import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico,Integer> {
    // Buscar por RUN
    Optional<Medico> findByRun(String run);

    //Buscar por nombre y apellido
    List<Medico> findByNombreAndApellido(String nombre, String apellido);

    // Buscar por nombre o apellido (insensible a mayúsculas)
    List<Medico> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

    // Médicos con menos de N años de antigüedad
    @Query("SELECT m FROM Medico m WHERE (YEAR(CURRENT_DATE()) - YEAR(m.fechaContrato)) < :anios")
    List<Medico> findMedicosMenosDeAniosAntiguedad(@Param("anios") Integer anios);

    // Médicos con más de N años de antigüedad
    @Query("SELECT m FROM Medico m WHERE (YEAR(CURRENT_DATE()) - YEAR(m.fechaContrato)) > :anios")
    List<Medico> findMedicosMasDeAniosAntiguedad(@Param("anios") Integer anios);
}
