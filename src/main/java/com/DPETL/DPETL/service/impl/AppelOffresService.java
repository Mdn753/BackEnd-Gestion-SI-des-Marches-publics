package com.DPETL.DPETL.service.impl;


import com.DPETL.DPETL.DTO.AppelOffresDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.AppelOffresDocuments;
import com.DPETL.DPETL.models.Gestionnaire;
import com.DPETL.DPETL.models.Offres;
import com.DPETL.DPETL.repositories.AppelOffresDocumentsRepository;
import com.DPETL.DPETL.repositories.AppelOffresRepository;
import com.DPETL.DPETL.repositories.GestionnaireRepository;
import com.DPETL.DPETL.service.AwsS3Service;
import com.DPETL.DPETL.service.interfac.IAppelOffresService;
import com.DPETL.DPETL.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppelOffresService implements IAppelOffresService {

    @Autowired
    private AppelOffresRepository appelOffresRepository;
    @Autowired
    private AppelOffresDocumentsRepository appelOffresDocumentsRepository;
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response AddAppelOffres(List<MultipartFile> files, AppelOffres appelOffres) {
        Response response = new Response();
        try {
            Gestionnaire currentGestionnaire = getAuthenticatedGestionnaire();
            if (currentGestionnaire == null) {
                throw new OurException("Authenticated gestionnaire is not found.");
            }

            appelOffres.setGestionnaire(currentGestionnaire);



            AppelOffres savedappelOffres = appelOffresRepository.save(appelOffres);

            List<AppelOffresDocuments> savedDocuments = files.stream().map(file -> {
                try {
                    String fileUrl = awsS3Service.saveAppelOffresDocumentsToS3(file);
                    AppelOffresDocuments appelOffresDocuments = new AppelOffresDocuments();
                    appelOffresDocuments.setNom(file.getOriginalFilename());
                    appelOffresDocuments.setPath(fileUrl);
                    appelOffresDocuments.setAppelOffres(appelOffres);

                    return appelOffresDocumentsRepository.save(appelOffresDocuments);
                } catch (Exception e) {
                    throw new RuntimeException("Error uploading file " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("AppelOffres and documents saved successfully.");
            response.setAppelOffresDTO(Utils.toAppelOffresDTO(savedappelOffres));
            response.setAppelOffresDocumentsDTOS(Utils.toListAppelOffresDocumentsDTO(savedDocuments));


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving an appelOffres " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response DeleteAppelOffres(Integer id) {
        Response response = new Response();
        try {
            AppelOffres appelOffres = appelOffresRepository.findById(id)
                    .orElseThrow(() -> new OurException("AppelOffre not existing"));
            for (AppelOffresDocuments document : appelOffres.getAppelsOffresdocuments()) {
                awsS3Service.deleteAppelOffresDocumentsFromS3Bucket(document.getPath());
            }
            appelOffresRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting AppelOffre " + e.getMessage());
        }


        return response;
    }

    @Override
    public Response GetAllAppelOffres() {
        Response response = new Response();
        try {
            List<AppelOffres> appelOffresList = appelOffresRepository.findAll();
            List<AppelOffresDTO> appelOffresDTOList = Utils.toListAppelOffresDTO(appelOffresList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setAppelOffresDTOS(appelOffresDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all appelOffres " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetAppelOffresById(Integer id) {
        Response response = new Response();
        try {
            AppelOffres appelOffres = appelOffresRepository.findById(id)
                    .orElseThrow(()-> new OurException("AppelOffre not existing"));
            AppelOffresDTO appelOffresDTO = Utils.toAppelOffresDTO(appelOffres);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setAppelOffresDTO(appelOffresDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting AppelOffre " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response UpdateAppelOffres(Integer id, AppelOffres appelOffres, List<MultipartFile> files) {
        Response response = new Response();
        try {
            // Retrieve the existing AppelOffres
            AppelOffres existingAppelOffres = appelOffresRepository.findById(id)
                    .orElseThrow(() -> new OurException("AppelOffre not existing"));

            // Update the fields of the existing AppelOffres with the new values
            existingAppelOffres.setAnnee(appelOffres.getAnnee());
            existingAppelOffres.setReference(appelOffres.getReference());
            existingAppelOffres.setObjet(appelOffres.getObjet());
            existingAppelOffres.setMontant(appelOffres.getMontant());
            existingAppelOffres.setDatePublication(appelOffres.getDatePublication());
            existingAppelOffres.setDateSignature(appelOffres.getDateSignature());
            existingAppelOffres.setBeneficiaire(appelOffres.getBeneficiaire());
            existingAppelOffres.setEtat(appelOffres.getEtat());
            // Update other fields as necessary

            // If new files are provided, handle the file uploads
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        // Upload the new file to S3
                        String fileUrl = awsS3Service.saveAppelOffresDocumentsToS3(file);

                        // Create a new AppelOffresDocuments entity and set its fields
                        AppelOffresDocuments newDocument = new AppelOffresDocuments();
                        newDocument.setNom(file.getOriginalFilename());
                        newDocument.setPath(fileUrl);
                        newDocument.setAppelOffres(existingAppelOffres);

                        // Add the new document to the list of documents
                        existingAppelOffres.getAppelsOffresdocuments().add(newDocument);

                        // Save the new document
                        appelOffresDocumentsRepository.save(newDocument);
                    }
                }
            }

            // Save the updated AppelOffres
            appelOffresRepository.save(existingAppelOffres);

            // Prepare the response
            response.setStatusCode(200);
            response.setMessage("AppelOffres updated successfully.");
            response.setAppelOffresDTO(Utils.toAppelOffresDTO(existingAppelOffres));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating AppelOffre " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response DeleteAppelOffresDocument(String Path) {
        Response response = new Response();
        try {
            AppelOffresDocuments document = appelOffresDocumentsRepository.findByPath(Path);
            awsS3Service.deleteAppelOffresDocumentsFromS3Bucket(Path);
            appelOffresDocumentsRepository.deleteById(document.getId());
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting Document " + e.getMessage());
        }


        return response;
    }

    @Override
    public Response GetAllAppelOffresByAnnee(Integer annee) {
        Response response = new Response();
        try {
            List<AppelOffres> appelOffresList = appelOffresRepository.findByAnnee(annee);
            response.setAppelOffresDTOS(Utils.toListAppelOffresDTO(appelOffresList));
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Filtering by annee " + e.getMessage());
        }
        return response;
    }




    private Gestionnaire getAuthenticatedGestionnaire() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return gestionnaireRepository.findByUsername(username);
        }
        return null;
    }

}
