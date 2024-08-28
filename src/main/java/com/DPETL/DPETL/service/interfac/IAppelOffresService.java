package com.DPETL.DPETL.service.interfac;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.AppelOffres;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAppelOffresService {

    Response AddAppelOffres(List<MultipartFile> files, AppelOffres appelOffres);
    Response DeleteAppelOffres(Integer id);
    Response GetAllAppelOffres();
    Response GetAppelOffresById(Integer id);
    Response UpdateAppelOffres(Integer id, AppelOffres appelOffres, List<MultipartFile> files);
    Response DeleteAppelOffresDocument(String Path);
    Response GetAllAppelOffresByAnnee(Integer annee);
}
