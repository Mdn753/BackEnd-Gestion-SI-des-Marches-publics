package com.DPETL.DPETL.service.impl;

import com.DPETL.DPETL.DTO.MarcheDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.Commission;
import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.models.MarcheDocuments;
import com.DPETL.DPETL.repositories.CommissionRepository;
import com.DPETL.DPETL.repositories.GestionnaireRepository;
import com.DPETL.DPETL.repositories.MarcheDocumentsRepository;
import com.DPETL.DPETL.repositories.MarcheRepository;
import com.DPETL.DPETL.service.AwsS3Service;
import com.DPETL.DPETL.service.interfac.IMarcheService;
import com.DPETL.DPETL.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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
        Response response = new Response();
        try {
            // Assuming you have a method to get the authenticated Gestionnaire or relevant entity for Marche
            Commission currentGestionnaire = getAuthenticatedCommission();
            if (currentGestionnaire == null) {
                throw new OurException("Authenticated gestionnaire is not found.");
            }

            // Set the authenticated Gestionnaire or another relevant entity if necessary
            marche.setAppelOffres(marche.getAppelOffres());  // Ensure the related AppelOffres is set

            // Save the Marche
            Marche savedMarche = marcheRepository.save(marche);

            // Save the related documents to S3 and in the database
            List<MarcheDocuments> savedDocuments = files.stream().map(file -> {
                try {
                    String fileUrl = awsS3Service.saveMarcheDocumentsToS3(file);
                    MarcheDocuments marcheDocuments = new MarcheDocuments();
                    marcheDocuments.setNom(file.getOriginalFilename());
                    marcheDocuments.setPath(fileUrl);
                    marcheDocuments.setMarche(marche);

                    return marcheDocumentsRepository.save(marcheDocuments);
                } catch (Exception e) {
                    throw new RuntimeException("Error uploading file " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Marche and documents saved successfully.");
            response.setMarcheDTO(Utils.toMarcheDTO(savedMarche));
            response.setMarcheDocumentsDTOS(Utils.toListMarcheDocumentsDTO(savedDocuments));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving the Marche " + e.getMessage());
        }

        return response;
    }

    private Commission getAuthenticatedCommission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return commissionRepository.findByUsername(username);
        }
        return null;
    }

}
