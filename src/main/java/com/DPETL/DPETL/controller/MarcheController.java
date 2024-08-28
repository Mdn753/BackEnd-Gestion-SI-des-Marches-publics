package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Marche;
import com.DPETL.DPETL.service.interfac.IMarcheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/marche")
public class MarcheController {

    @Autowired
    private IMarcheService marcheService;
    @Autowired
    private ObjectMapper objectMapper; // Inject the preconfigured ObjectMapper

    @PostMapping("/marche")
    @PreAuthorize("hasAuthority('Gestionnaire')")
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
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> DeleteMarche(
            @PathVariable Integer id
    ) {
        Response response = marcheService.DeleteMarche(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/marche")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> GetAllMarche() {
        Response response = marcheService.GetAllMarches();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/marche/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
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
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> DeleteMarcheDocument(
            @RequestParam String path
    ) {
        Response response = marcheService.DeleteMarcheDocuments(path);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
