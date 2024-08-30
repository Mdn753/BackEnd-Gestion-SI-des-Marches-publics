package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.models.MarcheDocuments;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MarcheRepository extends JpaRepository<Marche,Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM db_dptel.marche WHERE id = :id", nativeQuery = true)
    void deleteMarcheById(Integer id);
}
