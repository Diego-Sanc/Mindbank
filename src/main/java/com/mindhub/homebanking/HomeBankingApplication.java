package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomeBankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService,
									  TransactionService transactionService, LoanService loanService,
									  ClientLoanService clientLoanService, CardService cardService,
									  DynamicPinService dynamicPinService) {
		return (args) -> {

			Client melba = new Client("Melba", "Morel", passwordEncoder.encode("password"), "melba@mindhub.com","23232",true);
			Client jose = new Client("Jose", "Lopez",passwordEncoder.encode("wordpass"),"jose@mindhub.com","32432",true);
			clientService.saveClient(jose);
			clientService.saveClient(melba);
			DynamicPin dyna1 = new DynamicPin(dynamicPinService.randomDynaPin(),LocalDateTime.now());
			DynamicPin dyna2 = new DynamicPin(dynamicPinService.randomDynaPin(),LocalDateTime.now());

			dynamicPinService.saveDynaPin(dyna1);
			dynamicPinService.saveDynaPin(dyna2);

			dynamicPinService.setDynaPinToClient(dyna1, jose);
			dynamicPinService.setDynaPinToClient(dyna2, melba);
			Account vin001 = new Account(accountService.randomAccNumber(), LocalDateTime.now(),5000.0);
			Account vin002 = new Account(accountService.randomAccNumber(), LocalDateTime.now().plusDays(1),7500.0);
			melba.addAccount(vin001);
			melba.addAccount(vin002);
			Account vin003 = new Account(accountService.randomAccNumber(), LocalDateTime.now(),10000.0);
			Account vin004 = new Account(accountService.randomAccNumber(), LocalDateTime.now().plusDays(1),15000.0);
			jose.addAccount(vin003);
			jose.addAccount(vin004);
			accountService.saveAccount(vin001);
			accountService.saveAccount(vin002);
			accountService.saveAccount(vin003);
			accountService.saveAccount(vin004);
			Transaction trx001 = new Transaction(CardType.CREDIT,3000.0,"Hola Mundo",LocalDateTime.now());
			Transaction trx002 = new Transaction(CardType.DEBIT,-6000.0,"Lorem Ipsum",LocalDateTime.now());
			Transaction trx003 = new Transaction(CardType.CREDIT,10000.0,"Curso Mindhub",LocalDateTime.now());
			Transaction trx004 = new Transaction(CardType.DEBIT,-3000.0,"Compras Navidad",LocalDateTime.now());
			Transaction trx005 = new Transaction(CardType.CREDIT,15000.0,"Compras Steam",LocalDateTime.now());
			vin001.addTransaction(trx001);
			vin002.addTransaction(trx002);
			vin002.addTransaction(trx004);
			vin003.addTransaction(trx003);
			vin003.addTransaction(trx005);
			transactionService.saveTransaction(trx001);
			transactionService.saveTransaction(trx002);
			transactionService.saveTransaction(trx003);
			transactionService.saveTransaction(trx004);
			transactionService.saveTransaction(trx005);
			Loan hipotecario = new Loan("Hipotecario",500000, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal",100000, List.of(6,12,24));
			Loan automotriz = new Loan("Automotriz",300000, List.of(6,12,24,36));
			loanService.saveLoan(hipotecario);
			loanService.saveLoan(personal);
			loanService.saveLoan(automotriz);
			ClientLoan loan1 = new ClientLoan(melba,hipotecario,400000,60);
			ClientLoan loan2 = new ClientLoan(melba,personal,50000,12);
			ClientLoan loan3 = new ClientLoan(jose,personal,100000,24);
			ClientLoan loan4 = new ClientLoan(jose,automotriz,200000,36);
			clientLoanService.saveClientLoan(loan1);
			clientLoanService.saveClientLoan(loan2);
			clientLoanService.saveClientLoan(loan3);
			clientLoanService.saveClientLoan(loan4);
			Card card1 = new Card(melba.getFullName(),CardType.DEBIT,CardColor.GOLD,
					cardService.randomCardNumber(), cardService.randomCvv(), LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card2 = new Card(melba.getFullName(),CardType.CREDIT,CardColor.TITANIUM,
					cardService.randomCardNumber(),cardService.randomCvv(),LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card3 = new Card(jose.getFullName(),CardType.CREDIT,CardColor.SILVER,
					cardService.randomCardNumber(),cardService.randomCvv(),LocalDateTime.now().plusYears(5),LocalDateTime.now());
			melba.addCard(card1);
			melba.addCard(card2);
			jose.addCard(card3);
			cardService.saveCard(card1);
			cardService.saveCard(card2);
			cardService.saveCard(card3);
		};
	}
}
