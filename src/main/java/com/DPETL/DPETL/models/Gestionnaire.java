package com.DPETL.DPETL.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("Gestionnaire")
@Entity
public class Gestionnaire extends User{

    @OneToMany(
            mappedBy = "gestionnaire",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<AppelOffres> appelOffres = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "admin_id"
    )
    @JsonBackReference
    private Admin admin;
}
