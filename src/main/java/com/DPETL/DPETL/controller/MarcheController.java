package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.*;
import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.service.interfac.IMarcheService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MarcheController {

    @Autowired
    private IMarcheService marcheService;
    @Autowired
    private ObjectMapper objectMapper; // Inject the preconfigured ObjectMapper

    @PostMapping("/marche")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> AddMarche(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam("marche") String marcheJson
    ) throws IOException {
        Marche marche = objectMapper.readValue(marcheJson, Marche.class);
        if (files == null) {
            files = new ArrayList<>();
        }
        Response response = marcheService.AddMarche(files, marche);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/marche/{id}")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> DeleteMarche(
            @PathVariable Integer id
    ) {
        Response response = marcheService.DeleteMarche(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/marche")
    public ResponseEntity<Response> GetAllMarche() {
        Response response = marcheService.GetAllMarches();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/marche/{id}")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> UpdateMarche(
            @PathVariable Integer id,
            @RequestParam("marche") String marcheJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        Marche marche = objectMapper.readValue(marcheJson, Marche.class);
        if (files == null) {
            files = new ArrayList<>();
        }
        Response response = marcheService.UpdateMarche(id, marche, files);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/marche/document")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> DeleteMarcheDocument(
            @RequestParam String path
    ) {
        Response response = marcheService.DeleteMarcheDocuments(path);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/marche/documents/etape/{id}")
    public ResponseEntity<Response> GetDocumentsByEtape(
            @PathVariable Integer id,
            @RequestParam String etape
    ){
        Response response = marcheService.GetDocumentsByEtape(id,etape);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PostMapping("/marche/documents/upload")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> uploadDocuments(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam Map<String, String> params // Catch all params to handle descriptions and etapes
    ) throws IOException {

        // Extract descriptions and etapes from params
        List<String> descriptions = new ArrayList<>();
        List<String> etapes = new ArrayList<>();

        int index = 0;
        while (params.containsKey("descriptions[" + index + "]")) {
            descriptions.add(params.get("descriptions[" + index + "]"));
            etapes.add(params.get("etapes[" + index + "]"));
            index++;
        }

        // Extract marcheId
        Integer marcheId = Integer.parseInt(params.get("marcheId"));

        // Validate data
        if (files.size() != descriptions.size() || files.size() != etapes.size()) {
            throw new IllegalArgumentException("Mismatch between number of files, descriptions, and etapes.");
        }

        List<FileMetaData> fileMetadataList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String description = descriptions.get(i);
            String etape = etapes.get(i);

            FileMetaData fileMetadata = new FileMetaData();
            fileMetadata.setFile(file);
            fileMetadata.setDescription(description);
            fileMetadata.setEtape(etape);
            fileMetadata.setMarcheId(marcheId);

            fileMetadataList.add(fileMetadata);
        }

        // Call service method to process the combined data
        Response response = marcheService.UploadMarcheDocuments(fileMetadataList);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
