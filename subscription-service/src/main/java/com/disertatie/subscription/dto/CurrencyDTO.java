package com.disertatie.subscription.dto;

public enum CurrencyDTO
{
	EURO("EURO"),
	RON("RON"),
	USD("USD");

	private String text;

	CurrencyDTO(String text)
	{
		this.text = text;
	}

	public static String enumToString(String text)
	{
		for (CurrencyDTO currencyDTO : CurrencyDTO.values())
		{
			if (currencyDTO.text.equalsIgnoreCase(text))
				return currencyDTO.text;
		}
		return null;
	}
}
