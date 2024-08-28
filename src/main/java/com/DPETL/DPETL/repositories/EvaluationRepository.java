package com.DPETL.DPETL.repositories;

import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Evaluation;
import com.DPETL.DPETL.models.Offres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation,Integer> {
    Optional<Evaluation> findByOffre(Offres offres);

    @Query("SELECT e.offre.id FROM Evaluation e WHERE e.offre.appelOffres.id = :appelOffresId AND e.isWinning = true")
    Optional<Integer> findWinningOfferIdByAppelOffresId(@Param("appelOffresId") Integer appelOffresId);


    @Query("SELECT e FROM Evaluation e WHERE e.offre.appelOffres = :appelOffres AND e.isWinning = true")
    List<Evaluation> findByAppelOffresAndIsWinning(@Param("appelOffres") AppelOffres appelOffres);

    Optional<Evaluation> findByOffreIdAndIsWinningTrue(Integer offreId);

}
