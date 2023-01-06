package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.DynaPinDetailDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.DynamicPin;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.DynamicPinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping(value = "/api")
public class DynamicPinController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DynamicPinService dynamicPinService;

    @GetMapping("/clients/current/dynaPIN")
    public DynaPinDetailDTO getClientDynaPIN(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        DynamicPin dynamicPin = client.getDynamicPin();

        return new DynaPinDetailDTO(dynamicPin.getPin(), 60 - (int) ChronoUnit.SECONDS.between(dynamicPin.getUpdatedAt(),LocalDateTime.now()));
    }
}
