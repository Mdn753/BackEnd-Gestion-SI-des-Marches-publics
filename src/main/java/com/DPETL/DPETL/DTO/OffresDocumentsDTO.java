package com.DPETL.DPETL.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffresDocumentsDTO {

    private Integer id;
    private String nom;
    private String path;
    private String description;
    private String type;
    private MultipartFile file;  // Field to hold the actual file
    private OffresDTO offresDTO;
}
