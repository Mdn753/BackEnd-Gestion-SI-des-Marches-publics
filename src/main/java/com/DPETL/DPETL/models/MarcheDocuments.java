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
public class MarcheDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String path;
    private String Description;
    private String etape;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "marche_id"
    )
    @JsonBackReference
    private Marche marche;
}
