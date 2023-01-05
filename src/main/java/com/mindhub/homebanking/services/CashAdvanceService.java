package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CashAdvanceDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.CashAdvance;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CashAdvanceRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashAdvanceService {

    @Autowired
    private CashAdvanceRepository cashAdvanceRepository;

    public List<CashAdvanceDTO> getCashAdvancesDTO(){
        return cashAdvanceRepository.findAll().stream().map(CashAdvanceDTO::new).collect(Collectors.toList());
    }

    public CashAdvanceDTO getCashAdvanceDTOById(Long id){
        return cashAdvanceRepository.findById(id).map(CashAdvanceDTO::new).orElse(null);
    }

    public CashAdvance saveCashAdvance(CashAdvance advance){
        return cashAdvanceRepository.save(advance);
    }
}
