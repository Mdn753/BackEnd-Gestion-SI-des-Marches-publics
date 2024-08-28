package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.OffresDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffresDocumentsRepository extends JpaRepository<OffresDocuments,Integer> {
    OffresDocuments findByPath(String path);
}
