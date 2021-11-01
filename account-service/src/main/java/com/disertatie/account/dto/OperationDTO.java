package com.disertatie.account.dto;

import com.disertatie.account.model.Account;
import com.disertatie.account.model.Operation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
public class OperationDTO {
    private int id;
    private String type;
    private Double amount;
    private LocalDate date;
    private int clientId;
    private Account account;

    public static OperationDTO getDTO(Operation operation){
        return OperationDTO.builder()
                .id(operation.getId())
                .type(operation.getType())
                .amount(operation.getAmount())
                .date(operation.getDate())
                .clientId(operation.getClientId())
                .account(operation.getAccount())
                .build();
    }
}
