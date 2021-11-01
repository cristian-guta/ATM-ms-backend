package com.disertatie.subscription.dto;

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
    private String name;
    private Double amount;
    private String details;
    private int cliendId;
    private String currency = CurrencyDTO.RON.toString();
}
