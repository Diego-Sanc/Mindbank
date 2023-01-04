package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                           @RequestParam Double amount, @RequestParam String description,
                                           @RequestParam String dynaPIN, Authentication authentication){

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == null || amount<=0 || description.isEmpty()){
            return new ResponseEntity<>("Todos los campos son obligatorios y el monto no puede ser cero o negativo", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClientByEmail(authentication.getName());
        Account accountFrom = accountService.getAccountByNumber(fromAccountNumber);
        Account accountTo = accountService.getAccountByNumber(toAccountNumber);

        if (accountFrom == null || accountTo == null){
            return new ResponseEntity<>("Cuenta erronea, por favor verifica los datos.", HttpStatus.FORBIDDEN);
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

        if (!client.getDynamicPin().getPin().equals(dynaPIN)){
            return new ResponseEntity<>("Clave dinámica erronea", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(CardType.DEBIT, -amount, description +" - "+ accountTo.getNumber(), LocalDateTime.now());

        Transaction transactionCredit = new Transaction(CardType.CREDIT, amount, description +" - "+ accountFrom.getNumber(), LocalDateTime.now());

        transactionService.setTransactionToAccount(transactionDebit, accountFrom);
        transactionService.setTransactionToAccount(transactionCredit, accountTo);

        accountFrom.setBalance(accountFrom.getBalance()-amount);
        accountTo.setBalance(accountTo.getBalance()+amount);

        accountService.saveAccount(accountFrom);
        accountService.saveAccount(accountTo);
        transactionService.saveTransaction(transactionCredit);
        transactionService.saveTransaction(transactionDebit);

        return new ResponseEntity<>("Fondos transferidos", HttpStatus.CREATED);
    }


}
