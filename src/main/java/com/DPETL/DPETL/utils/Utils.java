package com.DPETL.DPETL.utils;


import com.DPETL.DPETL.DTO.*;
import com.DPETL.DPETL.models.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static AdminDTO toAdminDTO(Admin admin){
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(adminDTO.getId());
        adminDTO.setName(admin.getName());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setRole(admin.getRole());

//        if(!admin.getGestionnaire().isEmpty()){
//            adminDTO.setGestionnaireDTOS(admin.getGestionnaire().stream().map(Utils::toGestionnaireDTO).collect(Collectors.toList()));
//        }

        if (admin.getGestionnaire() != null && !admin.getGestionnaire().isEmpty()) {
            adminDTO.setGestionnaireDTOS(admin.getGestionnaire().stream()
                    .map(g -> {
                        GestionnaireDTO gDTO = new GestionnaireDTO();
                        gDTO.setId(g.getId());
                        gDTO.setName(g.getName());
                        gDTO.setUsername(g.getUsername());
                        gDTO.setRole(g.getRole());
                        // Do not set adminDTO to avoid recursion
                        return gDTO;
                    })
                    .collect(Collectors.toList()));
        }

//        if(!admin.getCommission().isEmpty()){
//            adminDTO.setCommissionDTOS(admin.getCommission().stream().map(Utils::toCommissionDTO).collect(Collectors.toList()));
//        }
        if (admin.getCommission() != null && !admin.getCommission().isEmpty()) {
            adminDTO.setCommissionDTOS(admin.getCommission().stream()
                    .map(g -> {
                        CommissionDTO gDTO = new CommissionDTO();
                        gDTO.setId(g.getId());
                        gDTO.setName(g.getName());
                        gDTO.setUsername(g.getUsername());
                        gDTO.setRole(g.getRole());
                        // Do not set adminDTO to avoid recursion
                        return gDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return adminDTO;
    }

    public static GestionnaireDTO toGestionnaireDTO(Gestionnaire gestionnaire){
        GestionnaireDTO gestionnaireDTO = new GestionnaireDTO();
        gestionnaireDTO.setId(gestionnaire.getId());
        gestionnaireDTO.setName(gestionnaire.getName());
        gestionnaireDTO.setUsername(gestionnaire.getUsername());
        gestionnaireDTO.setPassword(gestionnaire.getPassword());
        gestionnaireDTO.setRole(gestionnaire.getRole());
        //gestionnaireDTO.setAdminDTO(toAdminDTO(gestionnaire.getAdmin()));

        Admin admin = gestionnaire.getAdmin();
        if (admin != null) {
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(admin.getId());
            adminDTO.setName(admin.getName());
            adminDTO.setUsername(admin.getUsername());
            adminDTO.setRole(admin.getRole());
            gestionnaireDTO.setAdminDTO(adminDTO);
        }


        if(!gestionnaire.getAppelOffres().isEmpty()){
            gestionnaireDTO.setAppelOffresDTOS(gestionnaire.getAppelOffres().stream().map(Utils::toAppelOffresDTO).collect(Collectors.toList()));
        }

        return gestionnaireDTO;
    }

    public static CommissionDTO toCommissionDTO(Commission commission){
        CommissionDTO commissionDTO = new CommissionDTO();
        commissionDTO.setId(commission.getId());
        commissionDTO.setName(commission.getName());
        commissionDTO.setUsername(commission.getUsername());
        commissionDTO.setPassword(commission.getPassword());
        commissionDTO.setIspresident(commission.isIspresident());
        commissionDTO.setRole(commission.getRole());
        //commissionDTO.setAdminDTO(toAdminDTO(commission.getAdmin()));

        Admin admin = commission.getAdmin();
        if (admin != null) {
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(admin.getId());
            adminDTO.setName(admin.getName());
            adminDTO.setUsername(admin.getUsername());
            adminDTO.setRole(admin.getRole());
            commissionDTO.setAdminDTO(adminDTO);
        }


        if(!commission.getEvaluations().isEmpty()){
            commissionDTO.setEvaluationDTOS(commission.getEvaluations().stream().map(Utils::toEvaluationDTO).collect(Collectors.toList()));
        }

        return commissionDTO;
    }


    public static AppelOffresDTO toAppelOffresDTO(AppelOffres appelOffres) {
        AppelOffresDTO appelOffresDTO = new AppelOffresDTO();
        appelOffresDTO.setId(appelOffres.getId());
        appelOffresDTO.setAnnee(appelOffres.getAnnee());
        appelOffresDTO.setReference(appelOffres.getReference());
        appelOffresDTO.setObjet(appelOffres.getObjet());
        appelOffresDTO.setMontant(appelOffres.getMontant());
        appelOffresDTO.setDatePublication(appelOffres.getDatePublication());
        appelOffresDTO.setDateSignature(appelOffres.getDateSignature());
        appelOffresDTO.setBeneficiaire(appelOffres.getBeneficiaire());
        appelOffresDTO.setEtat(appelOffres.getEtat());

        if (appelOffres.getGestionnaire() != null) {
            appelOffresDTO.setGestionnaireDTO(toGestionnaireDTOWithoutAppelOffres(appelOffres.getGestionnaire()));
        }

        if (appelOffres.getAppelsOffresdocuments() != null && !appelOffres.getAppelsOffresdocuments().isEmpty()) {
            appelOffresDTO.setAppelOffresDocumentsDTOS(appelOffres.getAppelsOffresdocuments().stream()
                    .map(Utils::toAppelOffresDocumentsDTOWithoutAppelOffres)
                    .collect(Collectors.toList()));
        }

        // Avoid nested conversion here to prevent recursion issues
        appelOffresDTO.setOffresDTOS(appelOffres.getOffres().stream()
                .map(offres -> {
                    OffresDTO offresDTO = new OffresDTO();
                    offresDTO.setId(offres.getId());
                    offresDTO.setConcurrent(offres.getConcurrent());
                    if (offres.getOffresdocuments() != null && !offres.getOffresdocuments().isEmpty()) {
                        offresDTO.setOffresDocumentsDTOS(offres.getOffresdocuments().stream()
                                .map(Utils::toOffresDocumentsDTOWithoutOffres)
                                .collect(Collectors.toList()));
                    } else {
                        offresDTO.setOffresDocumentsDTOS(Collections.emptyList()); // Ensure it's not null
                    }
                    return offresDTO;
                })
                .collect(Collectors.toList()));

        return appelOffresDTO;
    }


    public static OffresDTO toOffresDTO(Offres offres) {
        OffresDTO offresDTO = new OffresDTO();
        offresDTO.setId(offres.getId());
        offresDTO.setConcurrent(offres.getConcurrent());
        // OffresDTO now contains other necessary fields without causing recursion
        if (offres.getEvaluation() != null) {
            EvaluationDTO evaluationDTO = toEvaluationDTO(offres.getEvaluation());
            offresDTO.setEvaluationDTO(evaluationDTO);
        }

        //if (offres.getAppelOffres() != null) {
        //    AppelOffresDTO appelOffresDTO = toAppelOffresDTO(offres.getAppelOffres());
        //    offresDTO.setAppelOffresDTO(appelOffresDTO);
        //}

        if (offres.getAppelOffres() != null) {
            SimpleAppelOffresDTO simpleAppelOffresDTO = getSimpleAppelOffresDTO(offres);
            offresDTO.setSimpleAppelOffresDTO(simpleAppelOffresDTO);
        }

        if (!offres.getOffresdocuments().isEmpty()) {
            offresDTO.setOffresDocumentsDTOS(offres.getOffresdocuments().stream()
                    .map(Utils::toOffresDocumentsDTOWithoutOffres)
                    .collect(Collectors.toList()));
        }

        return offresDTO;
    }

    private static SimpleAppelOffresDTO getSimpleAppelOffresDTO(Offres offres) {
        SimpleAppelOffresDTO simpleAppelOffresDTO = new SimpleAppelOffresDTO();
        simpleAppelOffresDTO.setId(offres.getAppelOffres().getId());
        simpleAppelOffresDTO.setAnnee(offres.getAppelOffres().getAnnee());
        simpleAppelOffresDTO.setReference(offres.getAppelOffres().getReference());
        simpleAppelOffresDTO.setObjet(offres.getAppelOffres().getObjet());
        simpleAppelOffresDTO.setMontant(offres.getAppelOffres().getMontant());
        simpleAppelOffresDTO.setDatePublication(offres.getAppelOffres().getDatePublication());
        simpleAppelOffresDTO.setDateSignature(offres.getAppelOffres().getDateSignature());
        simpleAppelOffresDTO.setBeneficiaire(offres.getAppelOffres().getBeneficiaire());
        simpleAppelOffresDTO.setEtat(offres.getAppelOffres().getEtat());
        return simpleAppelOffresDTO;
    }


    public static EvaluationDTO toEvaluationDTO(Evaluation evaluation) {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setWinning(evaluation.isWinning());

        if (evaluation.getOffre() != null) {
            evaluationDTO.setOffreId(evaluation.getOffre().getId());
        }

        if (evaluation.getCommission() != null) {
            evaluationDTO.setCommissionDTO(toCommissionDTO(evaluation.getCommission()));
            evaluationDTO.setCommissionStatus(null); // Commission is set, so status is null
        } else {
            evaluationDTO.setCommissionStatus("unevaluated"); // No commission assigned
        }

        return evaluationDTO;
    }

    public static EvaluationDTO toEvaluationDTOWithoutOffres(Evaluation evaluation) {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setWinning(evaluation.isWinning());

        if (evaluation.getCommission() != null) {
            evaluationDTO.setCommissionDTO(toCommissionDTO(evaluation.getCommission()));
            evaluationDTO.setCommissionStatus(null); // Commission is set, so status is null
        } else {
            evaluationDTO.setCommissionStatus("unevaluated"); // No commission assigned
        }

        return evaluationDTO;
    }



    public static AppelOffresDocumentsDTO toAppelOffresDocumentsDTO(AppelOffresDocuments appelOffresDocuments) {
        AppelOffresDocumentsDTO appelOffresDocumentsDTO = new AppelOffresDocumentsDTO();
        appelOffresDocumentsDTO.setId(appelOffresDocuments.getId());
        appelOffresDocumentsDTO.setNom(appelOffresDocuments.getNom());
        appelOffresDocumentsDTO.setPath(appelOffresDocuments.getPath());
        appelOffresDocumentsDTO.setAppelOffresDTO(toAppelOffresDTO(appelOffresDocuments.getAppelOffres()));

        return appelOffresDocumentsDTO;
    }

    public static OffresDocumentsDTO toOffresDocumentsDTO(OffresDocuments offresDocuments){
        OffresDocumentsDTO offresDocumentsDTO = new OffresDocumentsDTO();
        offresDocumentsDTO.setId(offresDocuments.getId());
        offresDocumentsDTO.setNom(offresDocuments.getNom());
        offresDocumentsDTO.setPath(offresDocuments.getPath());
        offresDocumentsDTO.setDescription(offresDocuments.getDescription());
        offresDocumentsDTO.setType(offresDocuments.getType());
        offresDocumentsDTO.setOffresDTO(toOffresDTO(offresDocuments.getOffres()));

        return offresDocumentsDTO;
    }


    public static OffresDocumentsDTO toOffresDocumentsDTOWithoutOffres(OffresDocuments offresDocuments){
        OffresDocumentsDTO offresDocumentsDTO = new OffresDocumentsDTO();
        offresDocumentsDTO.setId(offresDocuments.getId());
        offresDocumentsDTO.setNom(offresDocuments.getNom());
        offresDocumentsDTO.setPath(offresDocuments.getPath());
        offresDocumentsDTO.setDescription(offresDocuments.getDescription());
        offresDocumentsDTO.setType(offresDocuments.getType());
        // Do not include the Offres conversion to avoid recursion
        return offresDocumentsDTO;
    }



    public static List<GestionnaireDTO> toListGestionnaireDTO(List<Gestionnaire> gestionnaireList){
        return gestionnaireList.stream().map(Utils::toGestionnaireDTO).collect(Collectors.toList());
    }

    public static List<CommissionDTO> toListCommissionDTO(List<Commission> commissionList){
        return commissionList.stream().map(Utils::toCommissionDTO).collect(Collectors.toList());
    }

    public static List<AppelOffresDocumentsDTO> toListAppelOffresDocumentsDTO(List<AppelOffresDocuments> appelOffresDocumentsList){
        return appelOffresDocumentsList.stream().map(Utils::toAppelOffresDocumentsDTO).collect(Collectors.toList());
    }

    public static List<AppelOffresDTO> toListAppelOffresDTO(List<AppelOffres> appelOffresList){
        return appelOffresList.stream().map(Utils::toAppelOffresDTO).collect(Collectors.toList());
    }

    public static List<OffresDTO> toListOffresDTO(List<Offres> offresList){
        return offresList.stream().map(Utils::toOffresDTO).collect(Collectors.toList());
    }



    public static GestionnaireDTO toGestionnaireDTOWithoutAppelOffres(Gestionnaire gestionnaire) {
        if (gestionnaire == null) {
            return null;
        }

        GestionnaireDTO dto = new GestionnaireDTO();
        dto.setId(gestionnaire.getId());
        dto.setName(gestionnaire.getName());
        dto.setUsername(gestionnaire.getUsername());
        dto.setRole(gestionnaire.getRole());

        // Avoid setting properties that could cause recursion
        return dto;
    }

    public static AppelOffresDocumentsDTO toAppelOffresDocumentsDTOWithoutAppelOffres(AppelOffresDocuments documents) {
        if (documents == null) {
            return null;
        }

        AppelOffresDocumentsDTO dto = new AppelOffresDocumentsDTO();
        dto.setId(documents.getId());
        dto.setNom(documents.getNom());
        dto.setPath(documents.getPath());

        // Avoid setting properties that could cause recursion
        return dto;
    }


    public static OffresDTO toOffresDTONoAppelOffresDTO(Offres offres) {
        System.out.println("Converting Offres to OffresDTO (No AppelOffres)");
        OffresDTO offresDTO = new OffresDTO();
        offresDTO.setId(offres.getId());
        offresDTO.setConcurrent(offres.getConcurrent());

        if (offres.getEvaluation() != null) {
            EvaluationDTO evaluationDTO = toEvaluationDTO(offres.getEvaluation());
            offresDTO.setEvaluationDTO(evaluationDTO);
        }

        if (offres.getOffresdocuments() != null && !offres.getOffresdocuments().isEmpty()) {
            offresDTO.setOffresDocumentsDTOS(
                    offres.getOffresdocuments().stream()
                            .map(Utils::toOffresDocumentsDTO)
                            .collect(Collectors.toList())
            );
        }

        return offresDTO;
    }

}
