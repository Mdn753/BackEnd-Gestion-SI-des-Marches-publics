package com.DPETL.DPETL.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppelOffresDTO {

    private Integer id;
    private Integer annee;
    private String reference;
    private String objet;
    private BigInteger montant;
    private LocalDate datePublication;
    private LocalDate dateSignature;
    private String Beneficiaire;
    private String Etat;
    private GestionnaireDTO gestionnaireDTO;
    private List<AppelOffresDocumentsDTO> appelOffresDocumentsDTOS;
    private List<OffresDTO> offresDTOS;

}
