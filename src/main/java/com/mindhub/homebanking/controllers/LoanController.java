package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping(value = "/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanData, Authentication authentication){

        Client client = clientService.getClientByEmail(authentication.getName());
        Loan loan = loanService.getLoanById(loanData.getLoanId());
        Account account = accountService.getAccountByNumber(loanData.getToAccountNumber());

        if (!client.isEstadoCuenta()){
            return new ResponseEntity<>("Cuenta no verificada", HttpStatus.FORBIDDEN);
        }
        if (!loanService.validateLoan(loanData)){
            return new ResponseEntity<>("Todos los campos son obligatorios y el monto no puede ser cero ni negativo", HttpStatus.FORBIDDEN);
        }

        if (loan == null){
            return new ResponseEntity<>("Prestamo no existe", HttpStatus.FORBIDDEN);
        }

        if(loanData.getAmount()>loan.getMaxAmount()){
            return new ResponseEntity<>("Monto supera el maximo permitido para este prestamo", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanData.getPayments())){
            return new ResponseEntity<>("Numero de cuotas invalido para este prestamo", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Cuenta destino no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Cuenta origen no pertenece a este usuario.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(client,loan,(int) (loanData.getAmount()*1.2), loanData.getPayments());
        Transaction transaction = new Transaction(CardType.CREDIT, loanData.getAmount()*1.0, loan.getName() +" - loan approved", LocalDateTime.now(), account.getBalance()+loanData.getAmount());

        transactionService.setTransactionToAccount(transaction,account);
        account.setBalance(account.getBalance()+loanData.getAmount());

        accountService.saveAccount(account);
        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Prestamo creado",HttpStatus.CREATED);
    }
}
