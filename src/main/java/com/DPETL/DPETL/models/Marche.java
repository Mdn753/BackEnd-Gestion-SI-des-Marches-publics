package com.DPETL.DPETL.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Marche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer annee;
    private String reference;
    private String objet;
    private BigInteger montant;
    private String Services;

    @JsonProperty("typeMarche")
    private String Type_Marche;
    @JsonProperty("modeFinancement")
    private String Mode_Financement;
    private String Categorie;
    private String Delai;
    @JsonProperty("dateEnvoi")
    private LocalDate Date_Envoi;
    @JsonProperty("dateApprobation")
    private LocalDate Date_Approbation;
    private String Prestataire;
    private String Etat;
    @OneToOne
    @JoinColumn(
            name = "appelOffre_id"
    )
    @JsonBackReference
    private AppelOffres appelOffres;
    @OneToMany(
            mappedBy = "marche",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<MarcheDocuments> MarcheDocuments = new ArrayList<>();

}
