package com.DPETL.DPETL.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int id;
    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;

    private AdminDTO adminDTO;
    private GestionnaireDTO gestionnaireDTO;
    private CommissionDTO commissionDTO;

    private List<AdminDTO> adminDTOS;
    private List<GestionnaireDTO> gestionnaireDTOS;
    private List<CommissionDTO> commissionDTOS;

    private AppelOffresDTO appelOffresDTO;
    private MarcheDTO marcheDTO;
    private OffresDTO offresDTO;
    private EvaluationDTO evaluationDTO;

    private List<AppelOffresDTO> appelOffresDTOS;
    private List<MarcheDTO> marcheDTOS;
    private List<OffresDTO> offresDTOS;
    private List<EvaluationDTO> evaluationDTOS;

    private AppelOffresDocumentsDTO appelOffresDocumentsDTO;
    private MarcheDocumentsDTO marcheDocumentsDTO;
    private OffresDocumentsDTO offresDocumentsDTO;

    private List<AppelOffresDocumentsDTO> appelOffresDocumentsDTOS;
    private List<MarcheDocumentsDTO> marcheDocumentsDTOS;
    private List<OffresDocumentsDTO> offresDocumentsDTOS;
}
