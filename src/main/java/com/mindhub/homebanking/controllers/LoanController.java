package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> createLoan(@RequestBody LoanAplicationDTO loanData, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan;
        Optional<Loan> optLoan = loanRepository.findById(loanData.getLoanId());
        Optional<Account> optAccount = accountRepository.findByNumber(loanData.getToAccountNumber());
        Account account;

        if (!loanData.validate()){
            return new ResponseEntity<>("Todos los campos son obligatorios y el monto no puede ser cero ni negativo", HttpStatus.FORBIDDEN);
        }

        if (optLoan.isEmpty()){
            return new ResponseEntity<>("Prestamo no existe", HttpStatus.FORBIDDEN);
        }

        else{
            loan = optLoan.get();
        }

        if(loanData.getAmount()>loan.getMaxAmount()){
            return new ResponseEntity<>("Monto supera el maximo permitido para este prestamo", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanData.getPayments())){
            return new ResponseEntity<>("Numero de cuotas invalido para este prestamo", HttpStatus.FORBIDDEN);
        }

        if(optAccount.isEmpty()){
            return new ResponseEntity<>("Cuenta destino no existe", HttpStatus.FORBIDDEN);
        }

        else {
            account = optAccount.get();
        }

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Cuenta origen no pertenece a este usuario.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(client,loan,(int) (loanData.getAmount()*1.2), loanData.getPayments());
        Transaction transaction = new Transaction(CardType.CREDIT, loanData.getAmount()*1.0, loan.getName() +" - loan approved", LocalDateTime.now());

        transaction.setAccount(account);
        account.setBalance(account.getBalance()+loanData.getAmount());

        accountRepository.save(account);
        transactionRepository.save(transaction);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Prestamo creado",HttpStatus.CREATED);
    }
}
