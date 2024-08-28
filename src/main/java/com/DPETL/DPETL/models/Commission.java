package com.DPETL.DPETL.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@DiscriminatorValue("Commission")
@Entity
public class Commission extends User {

    private boolean ispresident;

    @OneToMany(
            mappedBy = "commission"
    )
    private List<Evaluation> evaluations = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "admin_id"
    )
    @JsonBackReference
    private Admin admin;

}
