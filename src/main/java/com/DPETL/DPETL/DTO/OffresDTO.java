package com.DPETL.DPETL.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffresDTO {
    private Integer id;
    private String Concurrent;
    private List<OffresDocumentsDTO> offresDocumentsDTOS;
    private SimpleAppelOffresDTO simpleAppelOffresDTO;
    private EvaluationDTO evaluationDTO;
}
