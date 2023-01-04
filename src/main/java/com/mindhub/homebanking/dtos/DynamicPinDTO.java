package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.DynamicPin;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

public class DynamicPinDTO {

    private Long id;

    private String pin;

    private Client client;

    private LocalDateTime updatedAt;

    public DynamicPinDTO(DynamicPin dynamicPin) {
        this.id = dynamicPin.getId();
        this.pin = dynamicPin.getPin();
        this.client = dynamicPin.getClient();
        this.updatedAt = dynamicPin.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
