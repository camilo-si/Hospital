package com.example.ActHos.ActividadHospital_19_05.service;

import com.example.ActHos.ActividadHospital_19_05.model.Especialidad;
import com.example.ActHos.ActividadHospital_19_05.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EspecialidadService {
    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> getAllEspecialidads(){
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> getEspecialidadById(Integer idEspecialidad){
        return especialidadRepository.findById(idEspecialidad);
    }

    public Especialidad saveEspecialidad(Especialidad especialidad){
        return especialidadRepository.save(especialidad);
    }

    public void deleteEspecialidad(Integer idEspecialidad){
        especialidadRepository.deleteById(idEspecialidad);
    }

    public long countEspecialidads(){
        return especialidadRepository.count();
    }

    public boolean existsEspecialidad(Integer idEspecialidad){
        return especialidadRepository.existsById(idEspecialidad);
    }

}
