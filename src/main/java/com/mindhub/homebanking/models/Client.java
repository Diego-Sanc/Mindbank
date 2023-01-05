package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToOne(mappedBy = "client",fetch = FetchType.EAGER)
    private DynamicPin dynamicPin = null;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<CashAdvance> advances = new HashSet<>();

    public Client() { }

    public Client(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DynamicPin getDynamicPin() {
        return dynamicPin;
    }

    public void setDynamicPin(DynamicPin dynamicPin) {
        this.dynamicPin = dynamicPin;
    }

    public Set<CashAdvance> getAdvances() {
        return advances;
    }

    public void setAdvances(Set<CashAdvance> advances) {
        this.advances = advances;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    @JsonIgnore
    public List<Loan> getLoans(){
        return clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public void addAdvance(CashAdvance advance){
        advance.setClient(this);
        advances.add(advance);
    }
}