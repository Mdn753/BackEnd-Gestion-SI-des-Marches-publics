package com.DPETL.DPETL.service.interfac;


import com.DPETL.DPETL.DTO.LoginRequest;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Admin;
import com.DPETL.DPETL.models.Commission;
import com.DPETL.DPETL.models.Gestionnaire;

public interface IUserService {

    Response login(LoginRequest loginRequest);
    Response CreateGestionnaire(Gestionnaire gestionnaire);
    Response CreateCommission(Commission commission);
    Response DeleteGestionnaire(Integer id);
    Response DeleteCommission(Integer id);
    Response FindGestionnaireById(Integer id);
    Response FindCommissionById(Integer id);
    Response GetAllGestionnaire();
    Response GetAllCommission();
    Response UpdateGestionnaire(Integer id, Gestionnaire gestionnaire);
    Response UpdateCommission(Integer id, Commission commission);
    Response GetAllUsers();
    Response CreateAdmin(Admin admin);
}
