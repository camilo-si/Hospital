package com.hospital.api.service;

import com.hospital.api.model.Estado;
import com.hospital.api.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EstadoService {
    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> getAllEstados(){
        return estadoRepository.findAll();
    }

    public Optional<Estado> getEstadoById(Integer idEstado){
        return estadoRepository.findById(idEstado);
    }

    public Estado saveEstado(Estado estado){
        return estadoRepository.save(estado);
    }

    public void deleteEstado(Integer idEstado){
        estadoRepository.deleteById(idEstado);
    }

    public long countEstados(){
        return estadoRepository.count();
    }

    public boolean existsEstado(Integer idEstado){
        return estadoRepository.existsById(idEstado);
    }
    
}
