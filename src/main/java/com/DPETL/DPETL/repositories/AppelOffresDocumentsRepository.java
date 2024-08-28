package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.AppelOffresDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppelOffresDocumentsRepository extends JpaRepository<AppelOffresDocuments,Integer> {
    AppelOffresDocuments findByPath(String Path);
}
