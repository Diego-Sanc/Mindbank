package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public List<LoanDTO> getLoansDTO(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    public Loan getLoanById(Long id){
        return loanRepository.findById(id).orElse(null);
    }

    public boolean validateLoan(LoanApplicationDTO loan){
        return !(loan.getLoanId() == null || loan.getAmount() == null || loan.getAmount() <= 0 || loan.getPayments() == null || loan.getToAccountNumber().isEmpty());
    }

    public Loan saveLoan(Loan loan){
        return loanRepository.save(loan);
    }

}
