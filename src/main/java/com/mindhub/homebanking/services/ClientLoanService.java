package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    public List<ClientLoanDTO> getClientLoansDTO(){
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    public ClientLoanDTO getClientLoanDTOById(Long id){
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    public ClientLoan saveClientLoan(ClientLoan clientLoan){
        return clientLoanRepository.save(clientLoan);
    }
}
