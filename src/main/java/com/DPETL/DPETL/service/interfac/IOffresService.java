package com.DPETL.DPETL.service.interfac;


import com.DPETL.DPETL.DTO.OffresDocumentsDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Offres;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOffresService {

    Response AddOffres(List<OffresDocumentsDTO> documentsDTOList, Offres offres);
    Response DeleteOffres(Integer id);
    Response GetAllOffresByAppelOffres(AppelOffres appelOffres);
    Response UpdateOffres(Integer id, Offres offres, List<OffresDocumentsDTO> documentsDTOList);
    Response DeleteOffresDocument(String path);
    Response updateDocumentDetails(Integer documentId, String description, String type);


}
