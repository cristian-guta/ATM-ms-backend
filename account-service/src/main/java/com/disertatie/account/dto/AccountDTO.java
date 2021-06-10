package com.disertatie.account.dto;

import com.disertatie.account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private int id;
    private Double amount;
    private String name;
    private String details;
    private int clientId;

    public static AccountDTO getDTO(Account account) {
        return AccountDTO.builder()
                .amount(account.getAmount())
                .name(account.getName())
                .details(account.getDetails())
                .id(account.getId())
                .clientId(account.getClientId()).build();
    }

}
