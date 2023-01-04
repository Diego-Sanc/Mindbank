package com.mindhub.homebanking.dtos;

public class DynaPinDetailDTO {

    private String pin;

    private Integer timeLeft;

    public DynaPinDetailDTO(String pin, Integer timeLeft) {
        this.pin = pin;
        this.timeLeft = timeLeft;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }
}
