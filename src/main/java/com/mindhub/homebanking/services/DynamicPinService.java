package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.DynamicPinDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.DynamicPin;
import com.mindhub.homebanking.repositories.DynamicPinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class DynamicPinService {

    @Autowired
    DynamicPinRepository dynamicPinRepository;

    @Autowired
    UtilService utilService;

    public List<DynamicPinDTO> getDynaPinsDTO(){
        return dynamicPinRepository.findAll().stream().map(DynamicPinDTO::new).collect(Collectors.toList());
    }

    public DynamicPinDTO getDynaPinDTOById(Long id){
        return dynamicPinRepository.findById(id).map(DynamicPinDTO::new).orElse(null);
    }

    public String randomDynaPin(){
        return String.format("%06d",utilService.randomNumber(999999));
    }

    public DynamicPin createDynaPin(String pin){
        return new DynamicPin(pin, LocalDateTime.now());
    }

    public DynamicPin saveDynaPin(DynamicPin dynamicPin){
        return dynamicPinRepository.save(dynamicPin);
    }

    public void setDynaPinToClient(DynamicPin dynamicPin, Client client){
        client.setDynamicPin(dynamicPin);
        dynamicPin.setClient(client);
        dynamicPinRepository.save(dynamicPin);
    }

    @Scheduled(fixedDelay = 60000)
    public void generateDynPassword(){
        List<DynamicPin> dynaPins = dynamicPinRepository.findAll();
        for (DynamicPin dynaPin : dynaPins) {
            dynaPin.setPin(randomDynaPin());
        }
        dynamicPinRepository.saveAll(dynaPins);
    }
}
