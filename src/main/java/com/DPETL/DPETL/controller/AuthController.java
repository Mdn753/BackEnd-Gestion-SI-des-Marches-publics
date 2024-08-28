package com.DPETL.DPETL.controller;


import com.DPETL.DPETL.DTO.LoginRequest;
import com.DPETL.DPETL.DTO.Response;
import com.DPETL.DPETL.service.interfac.IAppelOffresService;
import com.DPETL.DPETL.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAppelOffresService appelOffresService;



    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/appelOffres")
    //@PreAuthorize("hasAuthority('Gestionnaire')")
    public ResponseEntity<Response> GetAllAppelOffres(){
        Response response = appelOffresService.GetAllAppelOffres();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
