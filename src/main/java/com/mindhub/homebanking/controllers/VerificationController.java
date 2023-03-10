package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VerificationController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/verification")
    public String checkStatus(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        if (client.isEstadoCuenta()){
            return "verified";
        }
        else {
            return "notVerified";
        }
    }

    @PostMapping(value = "/verification")
    public ResponseEntity<Object> verify(@RequestParam String codVerify,Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        if(codVerify.isEmpty()){
            return new ResponseEntity<>("Por favor ingrese un codigo de verificacion", HttpStatus.FORBIDDEN);
        }if (!codVerify.equals(client.getCodVerficacion())){
            return new ResponseEntity<>("El codigo de verificacion no coincide",HttpStatus.FORBIDDEN);

        }
        client.setEstadoCuenta(true);
        clientService.saveClient(client);
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
