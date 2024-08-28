package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.AppelOffres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppelOffresRepository extends JpaRepository<AppelOffres,Integer> {
    Optional<AppelOffres> findById(Integer id);
    List<AppelOffres> findByAnnee(Integer annee);
}
