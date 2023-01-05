package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CashAdvanceApplicationDTO;
import com.mindhub.homebanking.dtos.CashAdvanceDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CashAdvanceController {
    @Autowired
    private CashAdvanceService cashAdvanceService;
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @RequestMapping(path = "/advances")
    public List<CashAdvanceDTO> getAdvances (){
        return cashAdvanceService.getCashAdvancesDTO();
    }
    @Transactional
    @RequestMapping(path = "/advances", method = RequestMethod.POST)
    public ResponseEntity<Object> requestCash (@RequestBody CashAdvanceApplicationDTO cashAdvanceApplicationDTO,
                                               Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        if (cashAdvanceApplicationDTO.getAmount()==null || cashAdvanceApplicationDTO.getPayments()==null ||
                cashAdvanceApplicationDTO.getAccountFinal().isEmpty() || cashAdvanceApplicationDTO.getCardId()==null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.getCardById(cashAdvanceApplicationDTO.getCardId());
        if(card==null){
            return new ResponseEntity<>("Tarjeta no existe", HttpStatus.FORBIDDEN);
        }
        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("La tarjeta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        if(cashAdvanceApplicationDTO.getAmount() > card.getAmount()){
            return new ResponseEntity<>("Monto solicitado excede el máximo", HttpStatus.FORBIDDEN);
        }
        if(card.getType().equals(CardType.DEBIT)){
            return new ResponseEntity<>("Tarjeta debe ser de crédito", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.getAccountByNumber(cashAdvanceApplicationDTO.getAccountFinal());
        if(account==null){
            return new ResponseEntity<>("Cuenta destino no existe", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        if(cashAdvanceApplicationDTO.getAmount()<= 0){
            return new ResponseEntity<>("No puedo solicitar monto negativo", HttpStatus.FORBIDDEN);
        }
        if (cashAdvanceApplicationDTO.getPayments()<1 || cashAdvanceApplicationDTO.getPayments()> 24){
            return new ResponseEntity<>("N° de cuotas inválido", HttpStatus.FORBIDDEN);
        }
        String hiddenNumber = String.format("XXXX XXXX XXXX %s",card.getNumber().substring(card.getNumber().length()-4));
        CashAdvance cashAdvance = new CashAdvance(cashAdvanceApplicationDTO.getAmount()*1.1,
                cashAdvanceApplicationDTO.getPayments(), LocalDateTime.now(), hiddenNumber,
                account.getNumber(),client);
        cashAdvanceService.saveCashAdvance(cashAdvance);
        card.setAmount(card.getAmount()-cashAdvanceApplicationDTO.getAmount()*1.1);
        cardService.saveCard(card);
        Transaction advance = new Transaction(CardType.CREDIT,cashAdvanceApplicationDTO.getAmount(),hiddenNumber + " cash advance approved ",
                LocalDateTime.now());
        transactionService.setTransactionToAccount(advance,account);
        transactionService.saveTransaction(advance);

        account.setBalance(account.getBalance()+cashAdvanceApplicationDTO.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
