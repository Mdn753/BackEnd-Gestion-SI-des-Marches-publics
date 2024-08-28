package com.DPETL.DPETL.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Offres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String Concurrent;

    @OneToMany(
            mappedBy = "offres",
            cascade = CascadeType.ALL
    )
    private List<OffresDocuments> Offresdocuments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "AppelOffres_id"
    )
    private AppelOffres appelOffres;

    @OneToOne(
            mappedBy = "offre",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private Evaluation evaluation;
}
