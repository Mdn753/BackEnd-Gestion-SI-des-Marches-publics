package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.models.Admin;
import com.DPETL.DPETL.models.Commission;
import com.DPETL.DPETL.models.Gestionnaire;
import com.DPETL.DPETL.service.interfac.IAppelOffresService;
import com.DPETL.DPETL.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAppelOffresService appelOffresService;




    @PostMapping("/admin/gestionnaire")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> CreateGestionnaire(
            @RequestBody Gestionnaire gestionnaire
            ){
        Response response = userService.CreateGestionnaire(gestionnaire);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/gestionnaire")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> GetAllGestionnaire(){
        Response response = userService.GetAllGestionnaire();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/gestionnaire/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> FindGestionnaireById(
            @PathVariable Integer id
    ){
        Response response = userService.FindGestionnaireById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/gestionnaire/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> DeleteGestionnaire(
            @PathVariable Integer id
    ){
        Response response = userService.DeleteGestionnaire(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/gestionnaire/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> UpdateGestionnaire(
            @PathVariable Integer id,
            @RequestBody Gestionnaire gestionnaire
    ){
        Response response = userService.UpdateGestionnaire(id, gestionnaire);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7777777


    @PostMapping("/admin/commission")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> CreateCommission(
            @RequestBody Commission commission
    ){
        Response response = userService.CreateCommission(commission);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/commission")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> GetAllCommission(){
        Response response = userService.GetAllCommission();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/commission/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> FindCommissionById(
            @PathVariable Integer id
    ){
        Response response = userService.FindCommissionById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/commission/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> DeleteCommission(
            @PathVariable Integer id
    ){
        Response response = userService.DeleteCommission(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/commission/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Response> UpdateCommission(
            @PathVariable Integer id,
            @RequestBody Commission commission
    ){
        Response response = userService.UpdateCommission(id, commission);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    @PostMapping("/admin")
    public ResponseEntity<Response> CreateAdmin(
            @RequestBody Admin admin
    ){
        Response response = userService.CreateAdmin(admin);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
