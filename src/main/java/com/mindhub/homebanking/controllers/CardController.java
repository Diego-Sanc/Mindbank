package com.mindhub.homebanking.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    ClientService clientService;

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType() == cardType).count()>=3){
            return new ResponseEntity<>("Max number of "+cardType+" cards reached", HttpStatus.FORBIDDEN);
        }
        else {
            String cardNumber = cardService.randomCardNumber();

            String cardCvv = cardService.randomCvv();

            Card card = new Card(client.getFullName(), cardType, cardColor, cardNumber, cardCvv,LocalDateTime.now().plusYears(5), LocalDateTime.now());

            cardService.setCardToClient(card, client);

            return new ResponseEntity<>("Created",HttpStatus.CREATED);
        }
    }
}
