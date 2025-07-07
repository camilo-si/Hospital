package com.example.ActHos.ActividadHospital_19_05.repository;

import com.example.ActHos.ActividadHospital_19_05.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Integer> {
    Optional<Estado> findByNombre(String nombre);
}
