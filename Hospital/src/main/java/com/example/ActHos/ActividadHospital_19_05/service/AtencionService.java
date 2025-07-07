package com.example.ActHos.ActividadHospital_19_05.service;


import com.example.ActHos.ActividadHospital_19_05.model.Atencion;
import com.example.ActHos.ActividadHospital_19_05.model.Estado;
import com.example.ActHos.ActividadHospital_19_05.repository.AtencionRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AtencionService {
    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Atencion> getAllAtencions(){
        return atencionRepository.findAll();
    }

    public Optional<Atencion> getAtencionById(Integer idAtencion){
        return atencionRepository.findById(idAtencion);
    }

    public Atencion saveAtencion(Atencion atencion){
        return atencionRepository.save(atencion);
    }

    public void deleteAtencion(Integer idAtencion){
        atencionRepository.deleteById(idAtencion);
    }

    public long countAtencions(){
        return atencionRepository.count();
    }

    public boolean existsAtencion(Integer idAtencion){
        return atencionRepository.existsById(idAtencion);
    }
    public List<Atencion> getAtencionesByFecha(LocalDate fecha) {
        return atencionRepository.findByFechaAtencion(fecha);
    }

    public List<Atencion> getAtencionesBetweenFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return atencionRepository.findByFechaAtencionBetween(fechaInicio, fechaFin);
    }

    public List<Atencion> getAtencionesCostoMenorQue(Integer cantidad) {
        return atencionRepository.findByCostoLessThan(cantidad);
    }

    public List<Atencion> getAtencionesCostoMayorQue(Integer cantidad) {
        return atencionRepository.findByCostoGreaterThan(cantidad);
    }

    public List<Atencion> getAtencionesByMedicoId(Integer idMedico){
        return atencionRepository.findByMedicoIdMedico(idMedico);
    }

    //* Mostrar el costo total de ganancia de un hospital según las atenciones que tengan un
    //estado de “Alta”.
    public Double getGananciaTotalPorAltas() {
        return atencionRepository.getTotalGananciaPorAltas();
    }

    //Consultar todas las atenciones que tuvo un paciente según su ID.
    public List<Atencion> obtenerAtencionesPorPaciente(Integer idPaciente) {
        return atencionRepository.findByPacienteIdPaciente(idPaciente);
    }

    //Mostrar las atenciones que tienen determinado estado.
    public List<Atencion> obtenerAtencionesPorEstado(String nombreEstado) {
        Estado estado = estadoRepository.findByNombre(nombreEstado.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + nombreEstado));
        return atencionRepository.findByEstado(estado);
    }


    //Mostrar el costo total que debe pagar un paciente determinado por sus atenciones.
    public Map<String, Object> calcularTotalPagoPorPaciente(Integer idPaciente) {
        Object[] resultado = atencionRepository.calcularTotalPagoPorPaciente(idPaciente);
        Map<String, Object> response = new HashMap<>();
        response.put("idPaciente", resultado[0]);
        response.put("totalAPagar", resultado[1]);
        return response;
    }
}
