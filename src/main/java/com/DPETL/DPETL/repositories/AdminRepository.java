package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    Admin findByUsername(String username);
    boolean existsByUsername(String username);
}
