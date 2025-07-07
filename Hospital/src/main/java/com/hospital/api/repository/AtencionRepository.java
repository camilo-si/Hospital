package com.hospital.api.repository;

import com.hospital.api.model.Atencion;
import com.hospital.api.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion,Integer> {
    // Buscar atenciones por fecha exacta
    List<Atencion> findByFechaAtencion(LocalDate fecha);

    // Buscar atenciones entre dos fechas
    List<Atencion> findByFechaAtencionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar atenciones con costo menor a X
    List<Atencion> findByCostoLessThan(Integer cantidad);

    // Buscar atenciones con costo mayor a X
    List<Atencion> findByCostoGreaterThan(Integer cantidad);

    List<Atencion> findByMedicoIdMedico (Integer idMedico);

    //* Mostrar el costo total de ganancia de un hospital según las atenciones que tengan un//estado de “Alta”.
    @Query("SELECT SUM(a.costo) FROM Atencion a WHERE a.estado.nombre = 'Alta'")
    Double getTotalGananciaPorAltas();

    //Consultar todas las atenciones que tuvo un paciente según su ID.
    List<Atencion> findByPacienteIdPaciente(Integer idPaciente);

    //Mostrar las atenciones que tienen determinado estado.
    List<Atencion> findByEstado(Estado estado);

    //Mostrar el costo total que debe pagar un paciente determinado por sus atenciones.
    @Query("SELECT p.idPaciente, SUM(a.costo * (1 - pr.cobertura)) " +
            "FROM Atencion a " +
            "JOIN a.paciente p " +
            "JOIN p.prevision pr " +
            "WHERE p.idPaciente = :idPaciente " +
            "GROUP BY p.idPaciente")
    Object[] calcularTotalPagoPorPaciente(@Param("idPaciente") Integer idPaciente);

    //mostrar total por atencion
    @Query("SELECT a FROM Atencion a WHERE a.paciente.idPaciente = :id")
    List<Atencion> findtotalcostoByPacienteId(@Param("id") Integer idPaciente);

}
