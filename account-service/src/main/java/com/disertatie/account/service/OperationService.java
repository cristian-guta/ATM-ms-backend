package com.disertatie.account.service;

import com.disertatie.account.dto.ClientDTO;
import com.disertatie.account.dto.OperationDTO;
import com.disertatie.account.dto.RoleDTO;
import com.disertatie.account.feign.ClientFeignResource;
import com.disertatie.account.feign.RoleFeignResource;
import com.disertatie.account.model.Account;
import com.disertatie.account.model.Operation;
import com.disertatie.account.repository.AccountRepository;
import com.disertatie.account.repository.OperationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperationService {
    private OperationRepository operationRepository;
    private ClientFeignResource clientFeignResource;
    private RoleFeignResource roleFeignResource;
    private AccountRepository accountRepository;

    public OperationService(OperationRepository operationRepository, ClientFeignResource clientFeignResource, RoleFeignResource roleFeignResource,
                            AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.clientFeignResource = clientFeignResource;
        this.roleFeignResource = roleFeignResource;
        this.accountRepository = accountRepository;
    }

    public OperationDTO findOperationById(String id) {
        Optional<Operation> optionalOperation = operationRepository.findById(Integer.parseInt(id));
        if (optionalOperation.isPresent()) {
            Operation operation = optionalOperation.get();
            return new OperationDTO(operation);
        } else {
            return new OperationDTO();
        }
    }

    public Page<OperationDTO> getAllOperations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        ClientDTO client = getAuthenticatedUser();

        Page<Operation> pageResult;
        RoleDTO role = roleFeignResource.getRoleById(client.getRoleId());
        if (!role.getName().equals("ADMIN")) {
            pageResult = operationRepository.findByClientId(client.getId(), pageRequest);
        } else {
            pageResult = operationRepository.findAll(pageRequest);
        }

        List<OperationDTO> operations = pageResult
                .stream()
                .map(OperationDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(operations, pageRequest, pageResult.getTotalElements());
    }

    public void createOperation(ClientDTO client, int accountId, int transferId, String type, Double amount) {

        LocalDate date = LocalDate.now();
        Account account = accountRepository.findAccountById(accountId);
        Operation operation = new Operation()
                .setAmount(amount)
                .setDate(date)
                .setType(type)
                .setAccount(account)
                .setClientId(client.getId());

        if (transferId != 0) {
            Account transfer = accountRepository.findAccountById(transferId);
            operation.setAccount(transfer);
//            emailService.createPDF(operation, principal, transfer);
        } else {
            operation.setAccount(account);
//            emailService.createPDF(operation, principal, null);
        }
        operationRepository.save(operation);
    }

    public ClientDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        if (clientFeignResource.getClientByUsername(login) == null) {
            return clientFeignResource.getClientByEmail(login);
        } else {
            return clientFeignResource.getClientByUsername(login);
        }
    }

}
