package com.DPETL.DPETL.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarcheDocumentsDTO {

    private Integer id;
    private String nom;
    private String path;
    private String etape;
    private MarcheDTO MarcheDTO;
}
