package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionDTO> getTransactionsDTO(){
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionDTOById(Long id){
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    public void setTransactionToAccount(Transaction transaction, Account account){
        account.addTransaction(transaction);
        transaction.setAccount(account);
        transactionRepository.save(transaction);
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
