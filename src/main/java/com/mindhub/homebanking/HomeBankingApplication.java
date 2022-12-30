package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {


			Client admin = new Client("Admin", "Istrator",passwordEncoder.encode("admin"),"adminmail@admin.com");
			Client melba = new Client("Melba", "Morel", passwordEncoder.encode("password"), "melba@mindhub.com");
			Client diego = new Client("Diego", "Sanchez", passwordEncoder.encode("wordpass"), "d.e.sanchez@accenture.com");
			clientRepository.save(admin);
			clientRepository.save(melba);
			clientRepository.save(diego);
			Account vin001 = new Account("VIN001", LocalDateTime.now(),5000.0);
			Account vin002 = new Account("VIN002", LocalDateTime.now().plusDays(1),7500.0);
			melba.addAccount(vin001);
			melba.addAccount(vin002);
			Account vin003 = new Account("VIN003", LocalDateTime.now(),10000.0);
			Account vin004 = new Account("VIN004", LocalDateTime.now().plusDays(1),15000.0);
			diego.addAccount(vin003);
			diego.addAccount(vin004);
			accountRepository.save(vin001);
			accountRepository.save(vin002);
			accountRepository.save(vin003);
			accountRepository.save(vin004);
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
			transactionRepository.save(trx001);
			transactionRepository.save(trx002);
			transactionRepository.save(trx003);
			transactionRepository.save(trx004);
			transactionRepository.save(trx005);
			Loan hipotecario = new Loan("Hipotecario",500000, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal",100000, List.of(6,12,24));
			Loan automotriz = new Loan("Automotriz",300000, List.of(6,12,24,36));
			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);
			ClientLoan loan1 = new ClientLoan(melba,hipotecario,400000,60);
			ClientLoan loan2 = new ClientLoan(melba,personal,50000,12);
			ClientLoan loan3 = new ClientLoan(diego,personal,100000,24);
			ClientLoan loan4 = new ClientLoan(diego,automotriz,200000,36);
			clientLoanRepository.save(loan1);
			clientLoanRepository.save(loan2);
			clientLoanRepository.save(loan3);
			clientLoanRepository.save(loan4);
			Card card1 = new Card(melba.getFullName(),CardType.DEBIT,CardColor.GOLD,
					"4658 2168 8746 5445","454",LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card2 = new Card(melba.getFullName(),CardType.CREDIT,CardColor.TITANIUM,
					"5498 2246 7954 1152","556",LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card3 = new Card(diego.getFullName(),CardType.CREDIT,CardColor.SILVER,
					"2465 7798 5332 1454","382",LocalDateTime.now().plusYears(5),LocalDateTime.now());
			melba.addCard(card1);
			melba.addCard(card2);
			diego.addCard(card3);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}
}
