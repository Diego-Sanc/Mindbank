package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                           @RequestParam Double amount, @RequestParam String description, Authentication authentication){

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == null || amount<=0 || description.isEmpty()){
            return new ResponseEntity<>("Todos los campos son obligatorios y el monto no puede ser cero o negativo", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        Optional<Account> optAccountFrom = accountRepository.findByNumber(fromAccountNumber);
        Optional<Account> optAccountTo = accountRepository.findByNumber(toAccountNumber);
        Account accountFrom;
        Account accountTo;

        if (optAccountFrom.isEmpty() || optAccountTo.isEmpty()){
            return new ResponseEntity<>("Cuenta erronea, por favor verifica los datos.", HttpStatus.FORBIDDEN);
        }
        else {
            accountFrom = optAccountFrom.get();
            accountTo = optAccountTo.get();
        }

        if (accountFrom.equals(accountTo)){
            return new ResponseEntity<>("Cuenta origen igual a la cuenta destino", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(accountFrom)){
            return new ResponseEntity<>("Cuenta origen no pertenece a este usuario.", HttpStatus.FORBIDDEN);
        }

        if (accountFrom.getBalance()<= amount){
            return new ResponseEntity<>("Cuenta sin fondos",HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(CardType.DEBIT, -amount, description +" - "+ accountTo.getNumber(), LocalDateTime.now());

        Transaction transactionCredit = new Transaction(CardType.CREDIT, amount, description +" - "+ accountFrom.getNumber(), LocalDateTime.now());

        transactionDebit.setAccount(accountFrom);
        transactionCredit.setAccount(accountTo);

        accountFrom.setBalance(accountFrom.getBalance()-amount);
        accountTo.setBalance(accountTo.getBalance()+amount);

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        return new ResponseEntity<>("Fondos transferidos", HttpStatus.CREATED);
    }


}
