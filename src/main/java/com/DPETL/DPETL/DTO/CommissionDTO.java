package com.DPETL.DPETL.DTO;

import com.DPETL.DPETL.models.Evaluation;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommissionDTO {

    private Integer id;
    @NotEmpty
    private String name;
    private boolean ispresident;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String role;
    List<EvaluationDTO> evaluationDTOS;
    private AdminDTO adminDTO;
}
