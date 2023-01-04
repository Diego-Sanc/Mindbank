package com.mindhub.homebanking.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamicpasswords")
public class DynamicPin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pin;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime updatedAt;

    public DynamicPin() {
    }

    public DynamicPin(String pin, LocalDateTime updatedAt) {
        this.pin = pin;
        this.updatedAt = updatedAt;
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

    @PreUpdate
    public void lastModified(){
        this.updatedAt = LocalDateTime.now();
    }
}
