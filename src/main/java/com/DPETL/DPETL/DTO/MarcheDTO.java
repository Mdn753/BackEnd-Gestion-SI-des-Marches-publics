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
    private String Services;
    private String Type_Marche;
    private String Mode_Financement;
    private String Categorie;
    private String Delai;
    private LocalDate Date_Envoi;
    private LocalDate Date_Approbation;
    private String Prestataire;
    private String Etat;
    private AppelOffresDTO appelOffresDTO;
    private List<MarcheDocumentsDTO> MarcheDocumentsDTOS;
}
