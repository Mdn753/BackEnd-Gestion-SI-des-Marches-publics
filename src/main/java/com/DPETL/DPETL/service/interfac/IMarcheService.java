package com.DPETL.DPETL.service.interfac;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Marche;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMarcheService {

    Response GetAllMarches();
    Response AddMarche(List<MultipartFile> files, Marche marche);
}
