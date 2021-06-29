package com.disertatie.account.service;

import com.disertatie.account.AccountNotFoundException;
import com.disertatie.account.dto.AccountDTO;
import com.disertatie.account.dto.ClientDTO;
import com.disertatie.account.dto.ResultDTO;
import com.disertatie.account.feign.ClientFeignResource;
import com.disertatie.account.model.Account;
import com.disertatie.account.repository.AccountRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private ClientFeignResource clientFeignResource;
    private OperationService operationService;

    public AccountService(AccountRepository accountRepository, ClientFeignResource clientFeignResource,
                          OperationService operationService) {
        this.accountRepository = accountRepository;
        this.clientFeignResource = clientFeignResource;
        this.operationService = operationService;
    }

    public AccountDTO getByCurrentClient(int id) {
        ClientDTO client = clientFeignResource.getClientById(id);
        Optional<Account> account = accountRepository.findAccountByClientId(client.getId());
        return account.map(AccountDTO::getDTO).orElseGet(AccountDTO::new);
    }

    public Page<AccountDTO> getAllAccounts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Account> pageResult = accountRepository.findAll(pageRequest);
        List<AccountDTO> accounts = pageResult
                .stream()
                .map(AccountDTO::getDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(accounts, pageRequest, pageResult.getTotalElements());
    }

    public AccountDTO createAccount(@RequestBody AccountDTO account) {

        Account newAccount = Account.getEntity(account);

        return AccountDTO.getDTO(accountRepository.save(newAccount));
    }

    public ResultDTO deleteAccount(int id) {
        accountRepository.deleteAccountById(id);
        return new ResultDTO().setStatus(true).setMessage("Account deleted!");
    }

    public AccountDTO getAccountById(int id) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findAccountById(id));
        if (account.isPresent()) {
            return AccountDTO.getDTO(account.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        }
    }

    public AccountDTO updateAccount(int id, AccountDTO accountDTO) {
        Account updateAccount = accountRepository.findAccountById(id);
        updateAccount.setId(accountDTO.getId())
                .setName(accountDTO.getName())
                .setAmount(accountDTO.getAmount())
                .setDetails(accountDTO.getDetails());

        return AccountDTO.getDTO(accountRepository.save(updateAccount));
    }

    public ResultDTO depositMoney(int accountId, Double amount, Principal principal) throws IOException {

        Account account = accountRepository.findAccountById(accountId);
        Double total = account.getAmount() + amount;
        account.setAmount(total);

        accountRepository.save(account);
        ClientDTO clientDTO = new ClientDTO();
        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            clientDTO = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            clientDTO = clientFeignResource.getClientByUsername(principal.getName());
        }

        operationService.createOperation(clientDTO, account.getId(), 0, "DEPOSIT", amount);
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO withdrawMoney(int accountId, double amount, Principal principal) throws IOException {

        Account account = accountRepository.findAccountById(accountId);
        try {
            Double total = account.getAmount() - amount;
            if (total < 0) {
                throw new RuntimeException("You want to withdraw more than you own! Throwing exception...");
            } else {
                account.setAmount(total);
                accountRepository.save(account);
                ClientDTO clientDTO = new ClientDTO();
                if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
                    clientDTO = clientFeignResource.getClientByEmail(principal.getName());
                } else {
                    clientDTO = clientFeignResource.getClientByUsername(principal.getName());
                }

                operationService.createOperation(clientDTO, accountId, 0, "WITHDRAW", amount);
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
        }
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO transferMoney(int senderAccountId, int receiverAccountId, Double amount, Principal principal) throws IOException {

        Account account = accountRepository.findAccountById(senderAccountId);

        Optional<Account> toSendTo = Optional.ofNullable(accountRepository.findAccountById(receiverAccountId));

        try {
            if(!toSendTo.isPresent()){
                throw new AccountNotFoundException("Account not found");
            }

            if (amount < account.getAmount()) {
                Double receiverAmount = toSendTo.get().getAmount() + amount;
                toSendTo.get().setAmount(receiverAmount);
                Double senderAmount = account.getAmount() - amount;
                account.setAmount(senderAmount);

                accountRepository.save(account);
                ClientDTO clientDTO = new ClientDTO();
                if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
                    clientDTO = clientFeignResource.getClientByEmail(principal.getName());
                } else {
                    clientDTO = clientFeignResource.getClientByUsername(principal.getName());
                }

                accountRepository.save(toSendTo.get());
                operationService.createOperation(clientDTO, senderAccountId, receiverAccountId, "TRANSFER", amount);

            } else {
                throw new RuntimeException("You want to transfer more than you own! Throwing exception...");
            }
        } catch (RuntimeException | AccountNotFoundException exc) {
            return new ResultDTO().setStatus(false).setMessage(exc.getMessage());
        }
        return new ResultDTO().setStatus(true).setMessage("Amount successfully transfered!");
    }
}
