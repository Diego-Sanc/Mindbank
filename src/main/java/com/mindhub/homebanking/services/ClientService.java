package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.DynamicPin;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<ClientDTO> getClientsDTO(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    public ClientDTO getClientDTOById(Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    public Client saveClient(Client client){
        return clientRepository.save(client);
    }
}
