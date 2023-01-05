package com.mindhub.homebanking.dtos;


public class CashAdvanceApplicationDTO {
    private Long cardId;
    private Double amount;
    private Integer payments;
    private String accountFinal;

    public CashAdvanceApplicationDTO(Long cardId, Double amount, Integer payments, String accountFinal){
        this.cardId = cardId;
        this.amount = amount;
        this.payments = payments;
        this.accountFinal = accountFinal;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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

    public String getAccountFinal() {
        return accountFinal;
    }

    public void setAccountFinal(String accountFinal) {
        this.accountFinal = accountFinal;
    }
}
