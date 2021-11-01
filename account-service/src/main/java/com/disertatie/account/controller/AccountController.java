package com.disertatie.account.controller;

import com.disertatie.account.AccountNotFoundException;
import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.CurrencyDTO;
import com.disertatie.account.dto.ResultDTO;
import com.disertatie.account.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@RestController
@NoArgsConstructor
@RequestMapping("/accounts")
public class AccountController
{
	@Autowired
	private AccountService accountService;

	@GetMapping("/client")
	public List<AccountDTO> getByCurrentCLient(@RequestParam(value = "id") int id)
	{
		return accountService.getByCurrentClient(id);
	}

	@GetMapping()
	public AccountDTO getAccountById(@RequestParam(value = "id") int id)
	{
		return accountService.getAccountById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{page}/{size}")
	public Page<AccountDTO> getAllAccounts(@PathVariable(value = "page") int page,
			@PathVariable(value = "size") int size)
	{
		return accountService.getAllAccounts(page, size);
	}

	@PostMapping
	public AccountDTO createAccount(@RequestBody AccountDTO newAccount)
	{
		return accountService.createAccount(newAccount);
	}

	@PutMapping("/{id}")
	public AccountDTO updateAccount(@PathVariable(value = "id") int id, @RequestBody AccountDTO accountDTO)
	{
		return accountService.updateAccount(id, accountDTO);
	}

	@PutMapping("/withdraw")
	public ResultDTO withdrawMoney(@RequestParam(value = "id") int id,
			@RequestParam(value = "amount") double amount,
			Principal principal) throws IOException
	{
		return accountService.withdrawMoney(id, amount, principal);
	}

	@PutMapping("/deposit")
	public ResultDTO depositMoney(@RequestParam(value = "id") int accountId,
			@RequestParam(value = "amount") double amount,
			Principal principal) throws IOException
	{
		return accountService.depositMoney(accountId, amount, principal);
	}

	@DeleteMapping
	public ResultDTO deleteAccount(@RequestParam(value = "id") int id)
	{
		return this.accountService.deleteAccount(id);
	}

	@PutMapping("/transfer")
	public ResultDTO transferMoney(@RequestParam(value = "senderId") int senderAccountId,
			@RequestParam(value = "receiverId") int receiverAccountId,
			@RequestParam(value = "amount") double amount,
			Principal principal) throws IOException
	{
		return accountService.transferMoney(senderAccountId, receiverAccountId, amount, principal);
	}

	@GetMapping("/currency")
	public AccountDTO getAccountByCurrency(@RequestParam("currency") String currency, @RequestParam("username") String username)
			throws AccountNotFoundException
	{
		return accountService.getClientAccountByCurrency(currency, username);
	}
}
