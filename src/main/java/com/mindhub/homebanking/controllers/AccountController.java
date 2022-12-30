package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService appService;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/accounts/{accountId}")
    public AccountDTO getAccountById(@PathVariable Long accountId){
        Optional<Account> optAccount = accountRepository.findById(accountId);
        return optAccount.map(AccountDTO::new).orElse(null);
    }
    @RequestMapping(value = "/clients/current/accounts")
    public List<AccountDTO> getClientAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size()>=3){
            return new ResponseEntity<>("Max number of accounts reached", HttpStatus.FORBIDDEN);
        }
        else {
            Account account = appService.createAccount(appService.randomAccNumber());
            appService.setAccountClient(account, client);
            return new ResponseEntity<>("Created",HttpStatus.CREATED);
        }
    }


}
