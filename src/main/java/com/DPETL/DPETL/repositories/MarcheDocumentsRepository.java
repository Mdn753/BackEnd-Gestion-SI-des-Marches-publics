package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.MarcheDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcheDocumentsRepository extends JpaRepository<MarcheDocuments,Integer> {
    MarcheDocuments findByPath(String Path);
    List<MarcheDocuments> findByMarcheIdAndEtape(Integer marcheId, String etape);
}
