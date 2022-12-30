package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UtilService utilService;


    public Account createAccount(String accNumber){
        Account account = new Account(accNumber, LocalDateTime.now(),0.0);
        return accountRepository.save(account);
    }

    public String randomAccNumber(){

        return String.format("VIN%08d",utilService.randomNumber(99999999));
    }

    public void setAccountToClient(Account account, Client client){
        account.setClient(client);
        client.addAccount(account);
        accountRepository.save(account);
    }

    public List<AccountDTO> getAccountsDTO(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public AccountDTO getAccountDTOById(Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    public Account getAccountByNumber(String number){
        return accountRepository.findByNumber(number).orElse(null);
    }

    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }
}
