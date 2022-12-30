package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;


    public Account createAccount(String accNumber){
        Account account = new Account(accNumber, LocalDateTime.now(),0.0);
        return accountRepository.save(account);
    }

    public String randomAccNumber(){
        return "VIN"+(int)(Math.random()*100000000);
    }

    public void setAccountClient(Account account, Client client){
        account.setClient(client);
        client.addAccount(account);
        accountRepository.save(account);
    }
}
