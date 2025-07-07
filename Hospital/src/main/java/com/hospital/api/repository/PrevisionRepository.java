package com.hospital.api.repository;

import com.hospital.api.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrevisionRepository  extends JpaRepository<Prevision,Integer> {
}
