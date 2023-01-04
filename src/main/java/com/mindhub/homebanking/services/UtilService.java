package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.DynamicPin;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.DynamicPinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class UtilService {

    @Autowired
    DynamicPinRepository dynamicPinRepository;

    public int randomNumber(int max){
        return (int)(Math.random()*(max+1));
    }
    public int randomNumber(int max, int min){
        return (int)(Math.random()*(max+1-min))+min;
    }

}
