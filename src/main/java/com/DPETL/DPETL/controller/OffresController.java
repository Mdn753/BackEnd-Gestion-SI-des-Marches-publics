package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.OffresDocumentsDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Offres;
import com.DPETL.DPETL.repositories.AppelOffresRepository;
import com.DPETL.DPETL.service.interfac.IOffresService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gestionnaire")
public class OffresController {

    @Autowired
    private IOffresService offresService;
    @Autowired
    private AppelOffresRepository appelOffresRepository;

    @PostMapping("/offres")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> AddOffres(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam("offres") String OffresJSON,
            @RequestParam("documents") String documentsJSON  // New parameter for documents metadata
    ) throws IOException {

        // Deserialize the Offres object from JSON
        Offres offres = new ObjectMapper().readValue(OffresJSON, Offres.class);

        // Deserialize the list of OffresDocumentsDTO from JSON
        List<OffresDocumentsDTO> documentsDTOList = new ObjectMapper().readValue(documentsJSON, new TypeReference<List<OffresDocumentsDTO>>() {});

        // Map the files to the corresponding documentsDTOList elements
        if (files != null && files.size() == documentsDTOList.size()) {
            for (int i = 0; i < files.size(); i++) {
                documentsDTOList.get(i).setFile(files.get(i));  // Assuming OffresDocumentsDTO has a MultipartFile field for the file
            }
        }

        // Call the service method with the updated list
        Response response = offresService.AddOffres(documentsDTOList, offres);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/offres/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> DeleteOffres(
            @PathVariable Integer id) {
        Response response = offresService.DeleteOffres(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/offres/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> UpdateOffres(
            @PathVariable Integer id,
            @RequestParam("offres") String OffresJSON,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam("documents") String documentsJSON // Add this parameter to accept documents metadata
    ) throws IOException {

        // Deserialize the Offres object from JSON
        Offres offres = new ObjectMapper().readValue(OffresJSON, Offres.class);

        // Deserialize the list of OffresDocumentsDTO from JSON
        List<OffresDocumentsDTO> documentsDTOList = new ObjectMapper().readValue(documentsJSON, new TypeReference<List<OffresDocumentsDTO>>() {});

        // Ensure the files and documentsDTOList have the same size
        if (files != null && files.size() == documentsDTOList.size()) {
            for (int i = 0; i < files.size(); i++) {
                documentsDTOList.get(i).setFile(files.get(i)); // Map each file to its metadata
            }
        }

        // Call the service method with the updated list
        Response response = offresService.UpdateOffres(id, offres, documentsDTOList);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/offres/document")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> DeleteOffresDocument(
            @RequestParam String path
    ) {
        Response response = offresService.DeleteOffresDocument(path);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/offres/document/{documentId}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> updateDocumentDetails(
            @PathVariable Integer documentId,
            @RequestBody OffresDocumentsDTO documentUpdateDTO) {

        String description = documentUpdateDTO.getDescription();
        String type = documentUpdateDTO.getType();
        Response response = offresService.updateDocumentDetails(documentId, description, type);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/offres")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> GetAllOffresByAppelOffres(
            @RequestParam Integer id
    ) {
        Optional<AppelOffres> optionalAppelOffres = appelOffresRepository.findById(id);
        Response response = offresService.GetAllOffresByAppelOffres(optionalAppelOffres.get());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}