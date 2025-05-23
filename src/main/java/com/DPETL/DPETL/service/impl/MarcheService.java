package com.DPETL.DPETL.service.impl;

import com.DPETL.DPETL.DTO.FileMetaData;
import com.DPETL.DPETL.DTO.MarcheDTO;
import com.DPETL.DPETL.DTO.MarcheDocumentsDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Commission;
import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.models.MarcheDocuments;
import com.DPETL.DPETL.repositories.*;
import com.DPETL.DPETL.service.AwsS3Service;
import com.DPETL.DPETL.service.interfac.IMarcheService;
import com.DPETL.DPETL.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
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
    private AppelOffresRepository appelOffresRepository;
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

    @Override
    public Response UpdateMarche(Integer id, Marche marche, List<MultipartFile> files) {
        Response response = new Response();
        try {
            // Retrieve the existing Marche
            Marche existingMarche = marcheRepository.findById(id)
                    .orElseThrow(() -> new OurException("Marche not found"));

            // Update the fields of the existing Marche with the new values
            existingMarche.setAnnee(marche.getAnnee());
            existingMarche.setReference(marche.getReference());
            existingMarche.setObjet(marche.getObjet());
            existingMarche.setMontant(marche.getMontant());
            existingMarche.setServices(marche.getServices());
            existingMarche.setType_Marche(marche.getType_Marche());
            existingMarche.setMode_Financement(marche.getMode_Financement());
            existingMarche.setCategorie(marche.getCategorie());
            existingMarche.setDelai(marche.getDelai());
            existingMarche.setDate_Envoi(marche.getDate_Envoi());
            existingMarche.setDate_Approbation(marche.getDate_Approbation());
            existingMarche.setPrestataire(marche.getPrestataire());
            existingMarche.setEtat(marche.getEtat());
            // Update other fields as necessary

            // If new files are provided, handle the file uploads
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        // Upload the new file to S3
                        String fileUrl = awsS3Service.saveMarcheDocumentsToS3(file);

                        // Create a new MarcheDocuments entity and set its fields
                        MarcheDocuments newDocument = new MarcheDocuments();
                        newDocument.setNom(file.getOriginalFilename());
                        newDocument.setPath(fileUrl);
                        newDocument.setMarche(existingMarche);

                        // Add the new document to the list of documents
                        existingMarche.getMarcheDocuments().add(newDocument);

                        // Save the new document
                        marcheDocumentsRepository.save(newDocument);
                    }
                }
            }

            // Save the updated Marche
            marcheRepository.save(existingMarche);

            // Prepare the response
            response.setStatusCode(200);
            response.setMessage("Marche updated successfully.");
            response.setMarcheDTO(Utils.toMarcheDTO(existingMarche));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating Marche " + e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional
    public Response DeleteMarche(Integer id) {
        Response response = new Response();
        try {
            // Retrieve the existing Marche
            Marche marche = marcheRepository.findById(id)
                    .orElseThrow(() -> new OurException("Marche not found"));

            // Log the retrieved Marche
            //System.out.println("Retrieved Marche: " + marche);

            // Retrieve and log the associated AppelOffres
            AppelOffres appelOffres = marche.getAppelOffres();
            if (appelOffres != null) {
                //System.out.println("Associated AppelOffres before update: " + appelOffres);
                appelOffres.setBeneficiaire(null);
                appelOffres.setEtat(null);
                appelOffresRepository.save(appelOffres);
                //System.out.println("Updated AppelOffres: " + appelOffres);
            }

            // Delete associated documents from S3
            for (MarcheDocuments document : marche.getMarcheDocuments()) {
                awsS3Service.deleteMarcheDocumentsFromS3Bucket(document.getPath());
            }


            marche.getMarcheDocuments().clear();
            marcheRepository.save(marche); // Save changes

            // Log before deletion
            //System.out.println("Deleting Marche with ID: " + id);

            // Delete the Marche entity
            marcheRepository.deleteMarcheById(id);

            // Log after deletion
            //System.out.println("Marche with ID: " + id + " deleted.");

            // Prepare the response
            response.setStatusCode(200);
            response.setMessage("Marche deleted successfully.");
            response.setId(id);  // Include the ID of the deleted Marche

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            response.setId(id);  // Include the ID for reference even if not found
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting Marche: " + e.getMessage());
            response.setId(id);  // Include the ID even in case of an error
        }

        return response;
    }



    @Override
    public Response DeleteMarcheDocuments(String path) {
        Response response = new Response();
        try {
            // Retrieve the MarcheDocuments entity by its path
            MarcheDocuments document = marcheDocumentsRepository.findByPath(path);

            // Delete the document from S3
            awsS3Service.deleteMarcheDocumentsFromS3Bucket(path);

            // Delete the document from the database
            marcheDocumentsRepository.deleteById(document.getId());

            // Prepare the response
            response.setStatusCode(200);
            response.setMessage("Marche document deleted successfully.");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting Marche document: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetDocumentsByEtape(Integer id, String etape) {
        Response response = new Response();
        try {
            List<MarcheDocuments> documents = marcheDocumentsRepository.findByMarcheIdAndEtape(id, etape);

            if (documents.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No documents found for the provided marche and etape.");
            } else {
                response.setStatusCode(200);
                response.setMessage("Documents retrieved successfully.");
                response.setMarcheDocumentsDTOS(Utils.toListMarcheDocumentsDTO(documents));
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting Documents: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response UploadMarcheDocuments(List<FileMetaData> fileMetaDataList) {
        Response response = new Response();

        try {

            List<MarcheDocumentsDTO> marcheDocumentsDTOList = new ArrayList<>();
            // Process each FileMetaData item
            for (FileMetaData fileMetaData : fileMetaDataList) {
                // Save the file to S3
                String fileUrl = awsS3Service.saveMarcheDocumentsToS3(fileMetaData.getFile());

                Marche existingMarche = marcheRepository.findById(fileMetaData.getMarcheId())
                        .orElseThrow(() -> new OurException("Marche not found"));;

                // Create a new MarcheDocumentsDTO object with the file metadata
                MarcheDocuments marcheDocuments = new MarcheDocuments();
                marcheDocuments.setNom(fileMetaData.getFile().getOriginalFilename());
                marcheDocuments.setPath(fileUrl);
                marcheDocuments.setDescription(fileMetaData.getDescription());
                marcheDocuments.setEtape(fileMetaData.getEtape());
                marcheDocuments.setMarche(existingMarche);

                marcheDocumentsRepository.save(marcheDocuments);
                marcheDocumentsDTOList.add(Utils.toMarcheDocumentsDTO(marcheDocuments));
            }

            response.setStatusCode(200);
            response.setMessage("Files uploaded and metadata saved successfully");
            response.setMarcheDocumentsDTOS(marcheDocumentsDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Adding Marche document: " + e.getMessage());
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
