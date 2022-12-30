package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService appService;

    @RequestMapping(value = "/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClientsDTO();
        //List<Client> --> List<ClientDTO>
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password){

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
        }
        if (clientService.getClientByEmail(email)!=null){
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName,lastName,passwordEncoder.encode(password),email);
        client = clientService.saveClient(client);
        Account account = appService.createAccount(appService.randomAccNumber());
        appService.setAccountToClient(account, client);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());

        return new ClientDTO(client);
    }




}
