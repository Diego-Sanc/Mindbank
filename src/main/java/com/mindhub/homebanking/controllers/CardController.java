package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    ClientService clientService;

    @GetMapping(value = "/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        return client.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }
    @GetMapping(value = "/clients/current/creditCards")
    public List<CardDTO> getCreditCards(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        return client.getCards().stream().filter(card -> card.getType().equals(CardType.CREDIT)).map(CardDTO::new).collect(Collectors.toList());
    }


    @PostMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType,
                                             @RequestParam(required = false) Double amount, Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());

        if (!client.isEstadoCuenta()){
            return new ResponseEntity<>("Cuenta no verificada", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().filter(card -> card.getType() == cardType).count()>=3){
            return new ResponseEntity<>("Max number of "+cardType+" cards reached", HttpStatus.FORBIDDEN);
        }
        else {
            String cardNumber = cardService.randomCardNumber();

            extracted();

            String cardCvv = cardService.randomCvv();

            if (cardType.equals(CardType.DEBIT)){
                amount = 0.00;
            } else if (amount==null || amount<= 0 || amount>1000000) {
                return new ResponseEntity<>("El monto es inválido o supera $1.000.000", HttpStatus.FORBIDDEN);
            }

            Card card = new Card(client.getFullName(), cardType, cardColor, cardNumber, cardCvv,LocalDateTime.now().plusYears(5), LocalDateTime.now(), amount);

            cardService.setCardToClient(card, client);

            return new ResponseEntity<>("Created",HttpStatus.CREATED);
        }
    }

    private void extracted() {
        String hola = "hola "+ "mundo " + "lorem " + "ipsum "+ cardService.randomCardNumber();
    }
}
