package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Offres;
import com.DPETL.DPETL.repositories.OffresRepository;
import com.DPETL.DPETL.service.interfac.IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;
    @Autowired
    private OffresRepository offresRepository;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Commission')")
    public ResponseEntity<Response> EvaluateOffre(
            @PathVariable Integer id,
            @RequestParam Boolean iswinning
    ){

        Response response = evaluationService.EvaluteOffre(id,iswinning);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{offreId}")
    public ResponseEntity<Response> GetWinningOffre(
            @PathVariable Integer offreId
    ){
        Response response = evaluationService.GetWinningOffre(offreId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
