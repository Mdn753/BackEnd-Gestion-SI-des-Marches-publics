package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.service.interfac.IAppelOffresService;
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
@RequestMapping("/gestionnaire")
public class AppelOffresController {

    @Autowired
    private IAppelOffresService appelOffresService;
    @Autowired
    private ObjectMapper objectMapper; // Inject the preconfigured ObjectMapper

    @PostMapping("/appelOffres")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> AddAppelOffres(
                @RequestParam(value = "files", required = false) List<MultipartFile> files,
                @RequestParam("appelOffres") String appelOffresJson
            ) throws IOException {
        AppelOffres appelOffres = objectMapper.readValue(appelOffresJson, AppelOffres.class);
        if (files == null) {
            files = new ArrayList<>();
        }
        Response response = appelOffresService.AddAppelOffres(files,appelOffres);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/appelOffres/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> DeleteAppelOffres(
            @PathVariable Integer id){

        Response response = appelOffresService.DeleteAppelOffres(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/appelOffres")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> GetAllAppelOffres(){
        Response response = appelOffresService.GetAllAppelOffres();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/appelOffres/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> GetAppelOffresById(
            @PathVariable Integer id
    ){
        Response response = appelOffresService.GetAppelOffresById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/appelOffres/{id}")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> UpdateAppelOffres(
            @PathVariable Integer id,
            @RequestParam("appelOffres") String appelOffresJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files

    ) throws IOException{

        AppelOffres appelOffres = objectMapper.readValue(appelOffresJson, AppelOffres.class);
        if (files == null) {
            files = new ArrayList<>();
        }
        Response response = appelOffresService.UpdateAppelOffres(id,appelOffres,files);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @DeleteMapping("/appelOffres/document")
    @PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> deleteAppelOffresDocument(
            @RequestParam String path
    ) {
        Response response = appelOffresService.DeleteAppelOffresDocument(path);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
