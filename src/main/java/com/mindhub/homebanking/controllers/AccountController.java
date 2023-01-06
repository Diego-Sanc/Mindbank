package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccountsDTO();
    }

    @GetMapping(value = "/accounts/{accountId}")
    public AccountDTO getAccountById(@PathVariable Long accountId){
        return accountService.getAccountDTOById(accountId);
    }
    @GetMapping(value = "/clients/current/accounts")
    public List<AccountDTO> getClientAccounts(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        if (!client.isEstadoCuenta()){
            return new ResponseEntity<>("Cuenta no verificada", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size()>=3){
            return new ResponseEntity<>("Max number of accounts reached", HttpStatus.FORBIDDEN);
        }
        else {
            Account account = accountService.createAccount(accountService.randomAccNumber());
            accountService.setAccountToClient(account, client);
            return new ResponseEntity<>("Created",HttpStatus.CREATED);
        }
    }


}
