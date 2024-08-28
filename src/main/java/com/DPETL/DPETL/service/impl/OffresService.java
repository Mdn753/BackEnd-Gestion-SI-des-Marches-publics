package com.DPETL.DPETL.service.impl;

import com.DPETL.DPETL.DTO.OffresDTO;
import com.DPETL.DPETL.DTO.OffresDocumentsDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.*;
import com.DPETL.DPETL.repositories.*;
import com.DPETL.DPETL.service.AwsS3Service;
import com.DPETL.DPETL.service.interfac.IOffresService;
import com.DPETL.DPETL.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OffresService implements IOffresService {

    @Autowired
    private OffresRepository offresRepository;
    @Autowired
    private OffresDocumentsRepository offresDocumentsRepository;
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private AppelOffresRepository appelOffresRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response AddOffres(List<OffresDocumentsDTO> documentsDTOList, Offres offres) {
        Response response = new Response();
        try {
            // Authenticate Gestionnaire
            Gestionnaire currentGestionnaire = getAuthenticatedGestionnaire();
            if (currentGestionnaire == null) {
                throw new OurException("Authenticated gestionnaire is not found.");
            }

            // Find associated AppelOffres
            AppelOffres associatedAppelOffres = appelOffresRepository.findById(offres.getAppelOffres().getId())
                    .orElseThrow(() -> new OurException("Associated AppelOffres not found"));
            offres.setAppelOffres(associatedAppelOffres);

            // Save the Offres entity
            Offres savedOffres = offresRepository.save(offres);

            // Handle the evaluation
            Evaluation evaluation = offres.getEvaluation();
            if (evaluation == null) {
                evaluation = new Evaluation();
                evaluation.setWinning(false); // Example default value
            }
            evaluation.setOffre(savedOffres);
            savedOffres.setEvaluation(evaluation);
            offresRepository.save(savedOffres);

            // Process and save each document along with its metadata
            List<OffresDocuments> savedDocuments = documentsDTOList.stream().map(dto -> {
                try {
                    MultipartFile file = dto.getFile();
                    String fileUrl = awsS3Service.saveOffresDocumentsToS3(file);

                    OffresDocuments offresDocuments = new OffresDocuments();
                    offresDocuments.setNom(file.getOriginalFilename());
                    offresDocuments.setPath(fileUrl);
                    offresDocuments.setDescription(dto.getDescription());  // Set the description
                    offresDocuments.setType(dto.getType());                // Set the type
                    offresDocuments.setOffres(savedOffres);

                    return offresDocumentsRepository.save(offresDocuments);
                } catch (Exception e) {
                    throw new RuntimeException("Error uploading file " + dto.getNom(), e);
                }
            }).collect(Collectors.toList());

            // Update the Offres entity with the saved documents
            savedOffres.setOffresdocuments(savedDocuments);
            offresRepository.save(savedOffres);

            // Set the success response
            response.setStatusCode(200);
            response.setMessage("Offres and documents added successfully.");
            response.setOffresDTO(Utils.toOffresDTO(savedOffres));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding Offre " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response DeleteOffres(Integer id) {
        Response response = new Response();
        try {
            Offres offres = offresRepository.findById(id)
                    .orElseThrow(()-> new OurException("Offres not existing"));

            for (OffresDocuments document : offres.getOffresdocuments()) {
                awsS3Service.deleteOffresDocumentsFromS3Bucket(document.getPath());
            }

            offresRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting Offre " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetAllOffresByAppelOffres(AppelOffres appelOffres) {
        Response response = new Response();
        try{
            List<Offres> offresList = new ArrayList<Offres>();
            offresList = offresRepository.findAllByAppelOffres(appelOffres);
            List<OffresDTO> offresDTOList = Utils.toListOffresDTO(offresList);

            response.setOffresDTOS(offresDTOList);
            response.setStatusCode(200);
            response.setMessage("Offres and documents added successfully.");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting Offre " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response UpdateOffres(Integer id, Offres offres, List<OffresDocumentsDTO> documentsDTOList) {
        Response response = new Response();
        try {
            Offres existingOffres = offresRepository.findById(id)
                    .orElseThrow(() -> new OurException("Offre not existing"));

            existingOffres.setConcurrent(offres.getConcurrent());

            if (documentsDTOList != null && !documentsDTOList.isEmpty()) {
                for (OffresDocumentsDTO documentDTO : documentsDTOList) {
                    MultipartFile file = documentDTO.getFile(); // Get the file associated with this document
                    if (file != null && !file.isEmpty()) {
                        // Upload the new file to S3
                        String fileUrl = awsS3Service.saveOffresDocumentsToS3(file);

                        // Create a new OffresDocuments entity and set its fields
                        OffresDocuments newDocument = new OffresDocuments();
                        newDocument.setNom(file.getOriginalFilename());
                        newDocument.setPath(fileUrl);
                        newDocument.setDescription(documentDTO.getDescription());
                        newDocument.setType(documentDTO.getType());
                        newDocument.setOffres(existingOffres);

                        // Add the new document to the list of documents
                        existingOffres.getOffresdocuments().add(newDocument);

                        // Save the new document
                        offresDocumentsRepository.save(newDocument);
                    }
                }
            }
            offresRepository.save(existingOffres);
            response.setStatusCode(200);
            response.setMessage("Offre updated successfully.");
            response.setOffresDTO(Utils.toOffresDTO(existingOffres));
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating Offre: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response DeleteOffresDocument(String path) {
        Response response = new Response();
        try{
            OffresDocuments document = offresDocumentsRepository.findByPath(path);
            awsS3Service.deleteOffresDocumentsFromS3Bucket(path);
            offresDocumentsRepository.deleteById(document.getId());
            response.setStatusCode(200);
            response.setMessage("successful");
        }  catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting Document " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateDocumentDetails(Integer documentId, String description, String type) {
        Response response = new Response();
        try {
            OffresDocuments document = offresDocumentsRepository.findById(documentId)
                    .orElseThrow(() -> new OurException("Document not found"));

            document.setDescription(description);
            document.setType(type);
            offresDocumentsRepository.save(document);

            response.setStatusCode(200);
            response.setMessage("Document details updated successfully.");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating document details: " + e.getMessage());
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
