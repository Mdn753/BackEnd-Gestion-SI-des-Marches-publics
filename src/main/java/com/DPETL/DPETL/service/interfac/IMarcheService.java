package com.DPETL.DPETL.service.interfac;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Marche;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMarcheService {
    Response GetAllMarches();
    Response AddMarche(List<MultipartFile> files, Marche marche);
    Response UpdateMarche(Integer id, Marche marche, List<MultipartFile> files);
    Response DeleteMarche(Integer id);
    Response DeleteMarcheDocuments(String path);
}
