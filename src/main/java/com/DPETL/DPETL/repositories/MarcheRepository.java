package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.models.MarcheDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcheRepository extends JpaRepository<Marche,Integer> {
}
