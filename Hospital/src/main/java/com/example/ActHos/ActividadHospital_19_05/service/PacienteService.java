package com.example.ActHos.ActividadHospital_19_05.service;

import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import com.example.ActHos.ActividadHospital_19_05.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> getAllPacientes(){
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> getPacienteById(Integer idPaciente){
        return pacienteRepository.findById(idPaciente);
    }

    public Paciente savePaciente(Paciente paciente){
        return pacienteRepository.save(paciente);
    }

    public void deletePaciente(Integer idPaciente){
        pacienteRepository.deleteById(idPaciente);
    }

    public long countPacientes(){
        return pacienteRepository.count();
    }

    public boolean existsPaciente(Integer idPaciente){
        return pacienteRepository.existsById(idPaciente);
    }

    public Paciente findByRun(String run){
        return pacienteRepository.findByRun(run);
    }

    public List<Paciente> findByNombreAndApellido(String nombre, String apellido){
        return pacienteRepository.findByNombreAndApellido(nombre,apellido);
    }

    public List<Paciente> findPatientsYoungerThan(Integer edad){
        return pacienteRepository.findPatientsYoungerThan(edad);
    }

    public List<Paciente> findPatientsOlderThan(Integer edad){
        return pacienteRepository.findPatientsOlderThan(edad);
    }

    //Mostrar los pacientes que tienen determinada previsión.
    public List<Paciente> obtenerPorIdPrevision(Integer idPrevision) {
        return pacienteRepository.findByPrevisionIdPrevision(idPrevision);
    }

    //Mostrar los pacientes que tienen determinada especialidad.
    public List<Paciente> obtenerPacientesPorEspecialidad(String nombreEspecialidad) {
        return pacienteRepository.findPacientesByEspecialidadNombre(nombreEspecialidad);
    }

    //Mostrar el costo total que debe pagar un paciente determinado por sus atenciones.
    //Esto se calcula sumando el costo de todas sus atenciones y descontando la
    //cobertura respectiva.


}
