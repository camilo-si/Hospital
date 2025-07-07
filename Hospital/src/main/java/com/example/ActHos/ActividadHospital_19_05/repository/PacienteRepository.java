package com.example.ActHos.ActividadHospital_19_05.repository;

import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Integer> {
    Paciente findByRun(String run);
    List<Paciente> findByNombreAndApellido(String nombre, String apellido);

    @Query(value = "SELECT * FROM paciente WHERE DATEDIFF(YEAR, fecha_nacimiento, GETDATE()) < :edad", nativeQuery = true)
    List<Paciente>findPatientsYoungerThan(@Param("edad") Integer edad);

    @Query(value = "SELECT * FROM paciente WHERE DATEDIFF(YEAR, fecha_nacimiento, GETDATE()) > :edad", nativeQuery = true)
    List<Paciente>findPatientsOlderThan(@Param("edad") Integer edad);

    //Mostrar los pacientes que tienen determinada previsión.
    List<Paciente> findByPrevisionIdPrevision(Integer idPrevision);

    //Mostrar los pacientes que tienen determinada especialidad.
    @Query("SELECT DISTINCT a.paciente FROM Atencion a WHERE a.medico.especialidad.nombre = :nombreEspecialidad")
    List<Paciente> findPacientesByEspecialidadNombre(@Param("nombreEspecialidad") String nombreEspecialidad);

    //Mostrar el costo total que debe pagar un paciente determinado por sus atenciones.
    //Esto se calcula sumando el costo de todas sus atenciones y descontando la
    //cobertura respectiva.

    /*@Query("SELECT CASE WHEN p.id_prevision = 1 THEN (SUM(a.costo) * 0.5) WHEN p.id_prevision = 2 THEN (SUM(a.costo) * 0.4) ELSE SUM(a.costo) END AS total, a.id_paciente" +
            " FROM atencion AS a LEFT JOIN paciente AS p ON a.id_paciente = p.id_paciente WHERE a.id_paciente = :idPaciente" +
            " GROUP BY a.id_paciente, p.id_prevision")
    List<Paciente> findCostoTotalByPaciente(@Param("idPaciente") Integer idPaciente);*/

}
