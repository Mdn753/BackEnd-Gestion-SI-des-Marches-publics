package com.DPETL.DPETL.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "offres_id"
    )
    @JsonBackReference
    private Offres offre;

    @ManyToOne
    @JoinColumn(
            name = "Commission"
    )
    private Commission commission;

    private boolean isWinning;
}
