package com.disertatie.account.controller;

import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.ResultDTO;
import com.disertatie.account.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/client")
    public AccountDTO getByCurrentCLient(@RequestParam(value = "id") int id) {
        return accountService.getByCurrentClient(id);
    }

    @GetMapping()
    public AccountDTO getAccountById(@RequestParam(value = "id") int id) {
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

    @PutMapping("/withdraw")
    public ResultDTO withdrawMoney(@RequestParam(value = "id") int id,
                                   @RequestParam(value = "amount") double amount,
                                   Principal principal) throws IOException {
        System.out.println(id + " " + amount);

        return accountService.withdrawMoney(id, amount, principal);
    }

    @PutMapping("/deposit")
    public ResultDTO depositMoney(@RequestParam(value = "id") int accountId,
                                  @RequestParam(value = "amount") double amount,
                                  Principal principal) {
        return accountService.depositMoney(accountId, amount, principal);
    }

    @DeleteMapping
    public ResultDTO deleteAccount(@RequestParam(value = "id") int id) {
        return this.accountService.deleteAccount(id);
    }

    @PutMapping("/transfer")
    public ResultDTO transferMoney(@RequestParam(value = "senderId") int senderAccountId,
                                   @RequestParam(value = "receiverId") int receiverAccountId,
                                   @RequestParam(value = "amount") double amount,
                                   Principal principal) throws IOException {
        return accountService.transferMoney(senderAccountId, receiverAccountId, amount, principal);
    }
}
