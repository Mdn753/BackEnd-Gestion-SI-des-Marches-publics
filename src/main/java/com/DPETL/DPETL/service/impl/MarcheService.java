package com.DPETL.DPETL.service.impl;

import com.DPETL.DPETL.DTO.MarcheDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.repositories.CommissionRepository;
import com.DPETL.DPETL.repositories.GestionnaireRepository;
import com.DPETL.DPETL.repositories.MarcheDocumentsRepository;
import com.DPETL.DPETL.repositories.MarcheRepository;
import com.DPETL.DPETL.service.AwsS3Service;
import com.DPETL.DPETL.service.interfac.IMarcheService;
import com.DPETL.DPETL.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MarcheService implements IMarcheService {

    @Autowired
    private MarcheRepository marcheRepository;
    @Autowired
    private MarcheDocumentsRepository marcheDocumentsRepository;
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response GetAllMarches() {
        Response response = new Response();
        try {
            List<Marche> marcheList = marcheRepository.findAll();
            List<MarcheDTO> marcheDTOList = Utils.toListMarcheDTO(marcheList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setMarcheDTOS(marcheDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all marches: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response AddMarche(List<MultipartFile> files, Marche marche) {
        return null;
    }


}
