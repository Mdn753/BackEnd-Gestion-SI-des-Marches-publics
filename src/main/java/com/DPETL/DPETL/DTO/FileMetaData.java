package com.DPETL.DPETL.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaData {
    private MultipartFile file;
    private String description;
    private String etape;
    private Integer marcheId;
}
