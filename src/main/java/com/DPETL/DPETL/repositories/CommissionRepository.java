package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepository extends JpaRepository<Commission,Integer> {

    boolean existsByUsername(String username);

    Commission findByUsername(String username);

}
