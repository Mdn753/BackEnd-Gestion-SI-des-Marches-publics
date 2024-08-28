package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire,Integer> {

    boolean existsByUsername(String username);

    Gestionnaire findByUsername(String username);

}
