package com.DPETL.DPETL.service.impl;


import com.DPETL.DPETL.DTO.*;
import com.DPETL.DPETL.exception.OurException;
import com.DPETL.DPETL.models.Admin;
import com.DPETL.DPETL.models.Commission;
import com.DPETL.DPETL.models.Gestionnaire;
import com.DPETL.DPETL.repositories.AdminRepository;
import com.DPETL.DPETL.repositories.CommissionRepository;
import com.DPETL.DPETL.repositories.GestionnaireRepository;
import com.DPETL.DPETL.repositories.UserRepository;
import com.DPETL.DPETL.service.interfac.IUserService;
import com.DPETL.DPETL.utils.JWTUtils;
import com.DPETL.DPETL.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new OurException("user Not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Occurred During user Login " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response CreateGestionnaire(Gestionnaire gestionnaire){
        Response response = new Response();
        try {
            Admin currentAdmin = getAuthenticatedAdmin();
            if (currentAdmin == null) {
                throw new OurException("Authenticated admin is not found.");
            }

            gestionnaire.setAdmin(currentAdmin);

            gestionnaire.setRole("Gestionnaire");
            if(gestionnaireRepository.existsByUsername(gestionnaire.getUsername())){
                throw new OurException(gestionnaire.getUsername() + "Already Exists");
            }

            gestionnaire.setPassword(passwordEncoder.encode(gestionnaire.getPassword()));
            Gestionnaire savedGestionnaire = gestionnaireRepository.save(gestionnaire);
            GestionnaireDTO gestionnaireDTO = Utils.toGestionnaireDTO(savedGestionnaire);
            response.setStatusCode(200);
            response.setGestionnaireDTO(gestionnaireDTO);

        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During gestionnaire Registration " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response CreateCommission(Commission commission){
        Response response = new Response();
        try {
            Admin currentAdmin = getAuthenticatedAdmin();
            if (currentAdmin == null) {
                throw new OurException("Authenticated admin is not found.");
            }

            commission.setAdmin(currentAdmin);

            commission.setRole("Commission");
            if(commissionRepository.existsByUsername(commission.getUsername())){
                throw new OurException(commission.getUsername() + "Already Exists");
            }

            commission.setPassword(passwordEncoder.encode(commission.getPassword()));
            Commission savedCommission = commissionRepository.save(commission);
            CommissionDTO commissionDTO = Utils.toCommissionDTO(savedCommission);
            response.setStatusCode(200);
            response.setCommissionDTO(commissionDTO);

        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During commission Registration " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response DeleteGestionnaire(Integer id){
        Response response = new Response();

        try {
            gestionnaireRepository.findById(id).orElseThrow(()->new OurException("Gestionnaire not found"));
            gestionnaireRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Deleting gestionnaire " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response DeleteCommission(Integer id){
        Response response = new Response();

        try {
            commissionRepository.findById(id).orElseThrow(()->new OurException("Commission not found"));
            commissionRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Deleting commission " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response FindGestionnaireById(Integer id){
        Response response = new Response();

        try {
            Gestionnaire gestionnaire = gestionnaireRepository.findById(id).orElseThrow(() -> new OurException("gestionnaire Not Found"));
            GestionnaireDTO gestionnaireDTO = Utils.toGestionnaireDTO(gestionnaire);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setGestionnaireDTO(gestionnaireDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting Gestionnaire " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response FindCommissionById(Integer id){
        Response response = new Response();

        try {
            Commission commission = commissionRepository.findById(id).orElseThrow(() -> new OurException("commission Not Found"));
            CommissionDTO commissionDTO = Utils.toCommissionDTO(commission);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCommissionDTO(commissionDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting Commission " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetAllGestionnaire() {
        Response response = new Response();
        try {
            List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
            List<GestionnaireDTO> gestionnaireDTOList = Utils.toListGestionnaireDTO(gestionnaireList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setGestionnaireDTOS(gestionnaireDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all gestionnaire " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetAllCommission() {
        Response response = new Response();

        try {
            List<Commission> commissionList = commissionRepository.findAll();
            List<CommissionDTO> commissionDTOList = Utils.toListCommissionDTO(commissionList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCommissionDTOS(commissionDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all Commission " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response UpdateGestionnaire(Integer id, Gestionnaire gestionnaire) {
        Response response = new Response();
        try {
            Gestionnaire existingGestionnaire = gestionnaireRepository.findById(id)
                    .orElseThrow(() -> new OurException("Gestionnaire not existing"));

            if (gestionnaire.getName() != null) {
                existingGestionnaire.setName(gestionnaire.getName());
            }
            if (gestionnaire.getUsername() != null) {
                existingGestionnaire.setUsername(gestionnaire.getUsername());
            }
            // Ensure password is handled securely
            if (gestionnaire.getPassword() != null) {
                existingGestionnaire.setPassword(gestionnaire.getPassword());
            }

            gestionnaireRepository.save(existingGestionnaire);

            response.setStatusCode(200);
            response.setMessage("Gestionnaire updated successfully.");
            response.setGestionnaireDTO(Utils.toGestionnaireDTO(existingGestionnaire));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating gestionnaire " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response UpdateCommission(Integer id, Commission commission) {
        Response response = new Response();
        try {
            Commission existingCommission = commissionRepository.findById(id)
                    .orElseThrow(() -> new OurException("Commission not existing"));

            existingCommission.setName(commission.getName());
            existingCommission.setIspresident(commission.isIspresident());
            existingCommission.setUsername(commission.getUsername());
            existingCommission.setPassword(commission.getPassword());

            commissionRepository.save(existingCommission);

            response.setStatusCode(200);
            response.setMessage("Commission updated successfully.");
            response.setCommissionDTO(Utils.toCommissionDTO(existingCommission));


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting Commission " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response GetAllUsers() {

        return null;
    }

    @Override
    public Response CreateAdmin(Admin admin) {
        Response response = new Response();
        try {

            admin.setRole("Admin");
            if(adminRepository.existsByUsername(admin.getUsername())){
                throw new OurException(admin.getUsername() + "Already Exists");
            }

            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin savedAdmin = adminRepository.save(admin);
            AdminDTO adminDTO = Utils.toAdminDTO(savedAdmin);
            response.setStatusCode(200);
            response.setAdminDTO(adminDTO);

        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During gestionnaire Registration " + e.getMessage());

        }
        return response;
    }


    private Admin getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return adminRepository.findByUsername(username);
        }
        return null;
    }
}
