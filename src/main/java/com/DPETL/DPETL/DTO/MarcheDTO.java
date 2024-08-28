package com.DPETL.DPETL.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarcheDTO {
    private Integer id;
    private Integer annee;
    private String reference;
    private String objet;
    private BigInteger montant;
    private LocalDate dateSignature;
    private String Prestataire;
    private String Etat;
    private AppelOffresDTO appelOffresDTO;
    private List<MarcheDocumentsDTO> MarcheDocumentsDTOS;
}
