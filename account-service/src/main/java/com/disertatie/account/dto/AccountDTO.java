package com.disertatie.account.dto;

import com.disertatie.account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private int id;
    private Double amount;
    private String name;
    private String details;
    private int clientId;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.amount = account.getAmount();
        this.details = account.getDetails();
        this.clientId = account.getClientId();
    }

}
