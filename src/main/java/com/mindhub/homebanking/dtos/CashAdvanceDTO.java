package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.CashAdvance;

import java.time.LocalDateTime;

public class CashAdvanceDTO {

    private Long id;
    private Double amount;
    private Integer payments;
    private LocalDateTime actualDate;
    private String accountInitial;
    private String accountFinal;

    public CashAdvanceDTO(CashAdvance cashAdvance) {
        this.id = cashAdvance.getId();
        this.amount = cashAdvance.getAmount();
        this.payments = cashAdvance.getPayments();
        this.actualDate = cashAdvance.getActualDate();
        this.accountInitial = cashAdvance.getAccountInitial();
        this.accountFinal = cashAdvance.getAccountFinal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
