package com.hospital.api.service;

import com.hospital.api.model.Prevision;
import com.hospital.api.repository.PrevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PrevisionService {
    @Autowired
    private PrevisionRepository previsionRepository;

    public List<Prevision> getAllPrevisions(){
        return previsionRepository.findAll();
    }

    public Optional<Prevision> getPrevisionById(Integer idPrevision){
        return previsionRepository.findById(idPrevision);
    }

    public Prevision savePrevision(Prevision prevision){
        return previsionRepository.save(prevision);
    }

    public void deletePrevision(Integer idPrevision){
        previsionRepository.deleteById(idPrevision);
    }

    public long countPrevisions(){
        return previsionRepository.count();
    }

    public boolean existsPrevision(Integer idPrevision){
        return previsionRepository.existsById(idPrevision);
    }
}
