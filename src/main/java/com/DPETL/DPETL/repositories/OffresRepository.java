package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Offres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffresRepository extends JpaRepository<Offres,Integer> {
    List<Offres> findAllByAppelOffres(AppelOffres appelOffres);
    List<Offres> findByAppelOffresAndIdNot(AppelOffres appelOffres,Integer id);
}
