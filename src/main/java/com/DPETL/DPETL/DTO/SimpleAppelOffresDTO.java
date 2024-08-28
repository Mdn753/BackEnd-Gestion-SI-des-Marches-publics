package com.DPETL.DPETL.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAppelOffresDTO {
    private Integer id;
    private Integer annee;
    private String Reference;
    private String objet;
    private BigInteger montant;
    private LocalDate datePublication;
    private LocalDate dateSignature;
    private String Beneficiaire;
    private String Etat;
}
