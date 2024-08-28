package com.DPETL.DPETL.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
@DiscriminatorValue("Admin")
@Entity
public class Admin extends User {

    @OneToMany(
            mappedBy = "admin",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Gestionnaire> gestionnaire;
    @OneToMany(
            mappedBy = "admin",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Commission> commission;
}
