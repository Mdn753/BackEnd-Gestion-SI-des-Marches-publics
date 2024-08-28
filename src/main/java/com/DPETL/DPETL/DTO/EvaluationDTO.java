package com.DPETL.DPETL.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationDTO {

    private Integer id;
    private boolean isWinning;
    private Integer offreId;
    private CommissionDTO commissionDTO;
    private String commissionStatus;

}
