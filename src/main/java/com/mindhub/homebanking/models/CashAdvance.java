package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_advances")
public class CashAdvance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double amount;
    private Integer payments;
    private LocalDateTime actualDate;
    private String accountInitial;
    private String accountFinal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public CashAdvance() {
    }

    public CashAdvance(Double amount,Integer payments, LocalDateTime actualDate, String accountInitial, String accountFinal,Client client) {
        this.amount = amount;
        this.payments = payments;
        this.actualDate = actualDate;
        this.accountInitial = accountInitial;
        this.accountFinal = accountFinal;
        this.client =client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public LocalDateTime getDate() {
        return actualDate;
    }

    public void setDate(LocalDateTime date) {
        this.actualDate = date;
    }

    public LocalDateTime getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDateTime actualDate) {
        this.actualDate = actualDate;
    }

    public String getAccountInitial() {
        return accountInitial;
    }

    public void setAccountInitial(String accountInitial) {
        this.accountInitial = accountInitial;
    }

    public String getAccountFinal() {
        return accountFinal;
    }

    public void setAccountFinal(String accountFinal) {
        this.accountFinal = accountFinal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
