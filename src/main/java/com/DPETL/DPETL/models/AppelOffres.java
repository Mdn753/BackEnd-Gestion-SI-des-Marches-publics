package com.DPETL.DPETL.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AppelOffres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer annee;
    private String reference;
    private String objet;
    private BigInteger montant;
    @ManyToOne
    @JoinColumn(
            name = "gestionnaire_id"
    )
    @JsonBackReference
    private Gestionnaire gestionnaire;
    @JsonFormat(pattern = "yyyy-MM-dd") // Format for LocalDate
    private LocalDate datePublication;
    @JsonFormat(pattern = "yyyy-MM-dd") // Format for LocalDate
    private LocalDate dateSignature;
    private String Beneficiaire;
    private String Etat;
    @OneToMany(
            mappedBy = "appelOffres",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL

    )
    @JsonManagedReference
    private List<AppelOffresDocuments> AppelsOffresdocuments = new ArrayList<>();
    @OneToMany(
            mappedBy = "appelOffres",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Offres> offres = new ArrayList<>();

}
