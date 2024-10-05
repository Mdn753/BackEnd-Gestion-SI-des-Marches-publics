package com.DPETL.DPETL.service.impl;

import com.DPETL.DPETL.DTO.OffresDTO;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.*;
import com.DPETL.DPETL.repositories.*;
import com.DPETL.DPETL.service.interfac.IEvaluationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EvaluationService implements IEvaluationService {

     @Autowired
     private OffresRepository offresRepository;
     @Autowired
     private EvaluationRepository evaluationRepository;
     @Autowired
     private CommissionRepository commissionRepository;
     @Autowired
     private AppelOffresRepository appelOffresRepository;
     @Autowired
     private MarcheRepository marcheRepository;


    @Override
    @Transactional
    public Response EvaluteOffre(Integer Offreid, boolean iswinning) {
        Response response = new Response();
        try {
            Commission currentCommission = getAuthenticatedCommission();
            if (currentCommission == null) {
                throw new OurException("Authenticated commission is not found.");
            }

            Optional<Offres> optionalOffre = offresRepository.findById(Offreid);
            if (optionalOffre.isEmpty()) {
                throw new OurException("Offre not found.");
            }

            Offres offre = optionalOffre.get();
            AppelOffres appelOffres = offre.getAppelOffres();

            if (iswinning) {
                // Clear any previous winning evaluations for this AppelOffres
                List<Evaluation> previousWinningEvaluations = evaluationRepository.findByAppelOffresAndIsWinning(appelOffres);
                for (Evaluation eval : previousWinningEvaluations) {
                    eval.setWinning(false);
                    evaluationRepository.save(eval);
                }

                // Update AppelOffres to reflect the new winner
                appelOffres.setBeneficiaire(offre.getConcurrent());
                appelOffres.setEtat("Attribuee");
                appelOffresRepository.save(appelOffres);

                // Create a new Marche associated with the winning AppelOffres
                Marche marche = new Marche();
                marche.setAnnee(appelOffres.getAnnee()); // Set appropriate year
                marche.setReference(appelOffres.getReference()); // Set appropriate reference
                marche.setObjet(appelOffres.getObjet()); // Set appropriate object
                marche.setMontant(appelOffres.getMontant()); // Set appropriate amount
                marche.setPrestataire(offre.getConcurrent()); // Set winning concurrent as prestataire
                marche.setEtat("Attribuee"); // Set appropriate state

                // Associate the Marche with the AppelOffres
                marche.setAppelOffres(appelOffres);

                // Save the Marche entity
                marcheRepository.save(marche);
            }

            // Handle the current offre evaluation
            Optional<Evaluation> existingEvaluation = evaluationRepository.findByOffre(offre);
            Evaluation evaluation;
            if (existingEvaluation.isPresent()) {
                // Update the existing evaluation
                evaluation = existingEvaluation.get();
                evaluation.setCommission(currentCommission);
                evaluation.setWinning(iswinning);
            } else {
                // Create a new evaluation
                evaluation = new Evaluation();
                evaluation.setOffre(offre);
                evaluation.setCommission(currentCommission);
                evaluation.setWinning(iswinning);
            }
            evaluationRepository.save(evaluation);

            response.setStatusCode(200);
            response.setMessage("Evaluation completed successfully");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Evaluating Offre: " + e.getMessage());
        }

        return response;
    }


    @Override
    public Response GetWinningOffre(Integer offreId) {
        Response response = new Response();
        try{
            Optional<Evaluation> winningEvaluation = evaluationRepository.findByOffreIdAndIsWinningTrue(offreId);

            if (winningEvaluation.isPresent()) {
                // Extract the ID of the winning offer
                Integer winningOffreId = winningEvaluation.get().getOffre().getId();

                // Create an OffresDTO object to hold the winning offer's ID
                OffresDTO winningOffresDTO = new OffresDTO();
                winningOffresDTO.setId(winningOffreId);

                // Set the OffresDTO in the response
                response.setOffresDTO(winningOffresDTO);
                response.setStatusCode(200);
                response.setMessage("Winning offer found.");
            } else {
                response.setStatusCode(404);
                response.setMessage("No winning offer found for the provided ID.");
            }

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Evaluating Offre: " + e.getMessage());
        }
        return response;
    }




    private Commission getAuthenticatedCommission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return commissionRepository.findByUsername(username);
        }
        return null;
    }
}
