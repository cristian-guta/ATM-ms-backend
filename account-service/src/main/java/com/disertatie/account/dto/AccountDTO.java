package com.disertatie.account.dto;

import com.disertatie.account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Currency;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO
{

	private int id;
	private Double amount;
	private String name;
	private String details;
	private int clientId;
	private String currency = CurrencyDTO.RON.toString();

	public static AccountDTO getDTO(Account account)
	{
		return AccountDTO.builder()
				.id(account.getId())
				.amount(account.getAmount())
				.name(account.getName())
				.details(account.getDetails())
				.clientId(account.getClientId())
				.currency(account.getCurrency()).build();
	}

}
