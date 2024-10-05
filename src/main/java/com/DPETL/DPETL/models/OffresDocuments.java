package com.DPETL.DPETL.models;

import com.DPETL.DPETL.models.Offres;
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
@Table(name = "offres_documents", uniqueConstraints = @UniqueConstraint(columnNames = "path"))
public class OffresDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Column(nullable = false, unique = true)  // Enforce unique constraint at the database level
    private String path;

    private String description;

    private String type;

    @ManyToOne()
    @JoinColumn(name = "offres_id")
    private Offres offres;
}
