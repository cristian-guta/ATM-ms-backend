package com.disertatie.account.controller;

import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.ResultDTO;
import com.disertatie.account.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/client/{id}")
    public AccountDTO getByCurrentCLient(@PathVariable("id") int id) {
        return accountService.getByCurrentClient(id);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable(value = "id") int id) {
        return accountService.getAccountById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{page}/{size}")
    public Page<AccountDTO> getAllAccounts(@PathVariable(value = "page") int page,
                                           @PathVariable(value = "size") int size) {
        return accountService.getAllAccounts(page, size);
    }

    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountDTO newAccount) {
        return accountService.createAccount(newAccount);
    }

    @PutMapping("/{id}")
    public AccountDTO updateAccount(@PathVariable(value = "id") int id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResultDTO withdrawMoney(@PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) throws IOException {
        return accountService.withdrawMoney(accountId, amount);
    }

    @PutMapping("/deposit/{id}/{amount}")
    public ResultDTO depositMoney(@PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) {
        return accountService.depositMoney(accountId, amount);
    }

    @DeleteMapping("/{id}")
    public ResultDTO deleteAccount(@PathVariable(value = "id") int id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping("/transfer/{senderAccountId}/{receiverAccountId}/{amount}")
    public ResultDTO transferMoney(@PathVariable(value = "senderAccountId") int senderAccountId, @PathVariable(value = "receiverAccountId") int receiverAccountId, @PathVariable(value = "amount") Double amount) throws IOException {
        return accountService.transferMoney(senderAccountId, receiverAccountId, amount);
    }
}
