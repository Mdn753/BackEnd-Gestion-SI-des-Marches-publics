package com.DPETL.DPETL.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AppelOffresDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appel_offres_id"
    )
    @JsonBackReference
    private AppelOffres appelOffres;
}
