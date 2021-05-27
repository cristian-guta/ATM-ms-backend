package com.disertatie.account.service;

import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.ClientDTO;
import com.disertatie.account.dto.ResultDTO;
import com.disertatie.account.feign.ClientFeignResource;
import com.disertatie.account.model.Account;
import com.disertatie.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private ClientFeignResource clientFeignResource;

    private Logger log = Logger.getLogger(AccountService.class.getName());

    @Autowired
    public AccountService(AccountRepository accountRepository, ClientFeignResource clientFeignResource) {
        this.accountRepository = accountRepository;
        this.clientFeignResource = clientFeignResource;
    }

    public Account getByCurrentClient(int id) {
        ClientDTO client = clientFeignResource.getClientById(id);
        return accountRepository.findAccountByClientId(client.getId());
    }

    public Page<AccountDTO> getAllAccounts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Account> pageResult = accountRepository.findAll(pageRequest);
        List<AccountDTO> accounts = pageResult
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(accounts, pageRequest, pageResult.getTotalElements());
    }

    public AccountDTO createAccount(@RequestBody AccountDTO account, Principal principal) {

        ClientDTO clientDTO = clientFeignResource.getClientByUsername(principal.getName());
        log.info("Creating account");
        Account newAccount = new Account()
                .setAmount(account.getAmount())
                .setName(account.getName())
                .setDetails(account.getDetails())
                .setClientId(clientDTO.getId());
        System.out.println(account);
        return new AccountDTO(accountRepository.save(newAccount));
    }

    public ResultDTO deleteAccount(int id) {
        accountRepository.deleteAccountById(id);
        return new ResultDTO().setStatus(true).setMessage("Account deleted!");
    }

    public AccountDTO getAccountById(int id) {
        Account account = accountRepository.findAccountById(id);
        if (account != null) {
            AccountDTO acc = new AccountDTO()
                    .setId(account.getId())
                    .setDetails(account.getDetails())
                    .setCliendId(account.getClientId())
                    .setAmount(account.getAmount())
                    .setName(account.getName());

            return acc;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        }
    }

    public AccountDTO updateAccount(int id, AccountDTO accountDTO) {
        System.out.println("Updating account");
        Account updateAccount = accountRepository.findAccountById(id);
        updateAccount.setId(accountDTO.getId())
                .setName(accountDTO.getName())
                .setAmount(accountDTO.getAmount())
                .setDetails(accountDTO.getDetails());
        accountRepository.save(updateAccount);

        return new AccountDTO(updateAccount);
    }

    public ResultDTO depositMoney(int accountId, Double amount) throws IOException {

        Account account = accountRepository.findAccountById(accountId);
        Double total = account.getAmount() + amount;
        account.setAmount(total);

        accountRepository.save(account);

//        operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO withdrawMoney(int accountId, Double amount) throws IOException {

        Account account = accountRepository.findAccountById(accountId);
        try {
            Double total = account.getAmount() - amount;
            if (total < 0) {
                throw new RuntimeException("You want to withdraw more than you own! Throwing exception...");
            } else {
                account.setAmount(total);
                accountRepository.save(account);
//                operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
        }
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO transferMoney(int senderAccountId, int receiverAccountId, Double amount) throws IOException {

        Account account = accountRepository.findAccountById(senderAccountId);
        Account toSendTo = accountRepository.findAccountById(receiverAccountId);

        try {
            if (amount < account.getAmount()) {
                Double senderAmount = account.getAmount() - amount;
                account.setAmount(senderAmount);

                accountRepository.save(account);

                Double receiverAmount = toSendTo.getAmount() + amount;
                toSendTo.setAmount(receiverAmount);

                accountRepository.save(toSendTo);

//                operationService.createOperation(principal, account.getId(), toSendTo.getId(), "transfer", amount);
            } else {
                throw new RuntimeException("You want to transfer more than you own! Throwing exception...");
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
        }

        return new ResultDTO().setStatus(true).setMessage("Amount successfully transfered!");
    }
}
