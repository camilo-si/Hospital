package com.example.ActHos.ActividadHospital_19_05.repository;

import com.example.ActHos.ActividadHospital_19_05.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad,Integer> {
}
