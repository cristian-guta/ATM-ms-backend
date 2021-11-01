package com.disertatie.account.model;

import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.CurrencyDTO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Data
@Accessors(chain = true)
@Entity
@Table(name = "account")
@Builder
public class Account
{

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

	private String currency = CurrencyDTO.RON.toString();

	public Account()
	{
	}

	public Account(int id, String name, Double amount, String details, int clientId, String currency)
	{
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.details = details;
		this.clientId = clientId;
		this.currency = currency;
	}

	public static Account getEntity(AccountDTO accountDTO)
	{
		return Account.builder()
				.id(accountDTO.getId())
				.name(accountDTO.getName())
				.amount(accountDTO.getAmount())
				.details(accountDTO.getDetails())
				.clientId(accountDTO.getClientId())
				.currency(accountDTO.getCurrency()).build();
	}
}
