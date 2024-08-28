package com.DPETL.DPETL.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GestionnaireDTO {

    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String role;
    private List<AppelOffresDTO> appelOffresDTOS;
    private AdminDTO adminDTO;

}
