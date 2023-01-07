package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UtilService utilService;

    public List<CardDTO> getCardsDTO(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    public CardDTO getCardDTOById(Long id){
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    public Card getCardById(Long id){
        return cardRepository.findById(id).orElse(null);
    }

    public String randomCardNumber(){
        return String.format("%04d %04d %04d %04d",utilService.randomNumber(9999),
                utilService.randomNumber(9999), utilService.randomNumber(9999),
                utilService.randomNumber(9999));
    }

    public String randomCvv(){
        return String.format("%03d",utilService.randomNumber(999));
    }

    public void setCardToClient(Card card, Client client){
        card.setClient(client);
        client.addCard(card);
        cardRepository.save(card);
    }

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }

    public void deleteCard(Card card){
        cardRepository.delete(card);
    }
}
