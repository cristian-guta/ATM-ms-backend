package com.disertatie.account.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @NotNull
    private Double amount;

    @Column
    private String details;

    @Column
    private int clientId;

    public Account() {
    }

    public Account(int id, String name, Double amount, String details, int clientId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.details = details;
        this.clientId = clientId;
    }
}