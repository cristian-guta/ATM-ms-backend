package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@FeignClient(name = "account-service", url = "http://localhost:8765")
public interface AccountFeignResource {

//    @RequestMapping("/account-service/accounts/client/{clientId}")
//    AccountDTO getClientBankAccount(@PathVariable int clientId);

    @GetMapping("/account-service/accounts/client/{id}")
    AccountDTO getClientBankAccount(@PathVariable("id") int id);

    @PutMapping("/account-service/accounts/update/{id}")
    AccountDTO updateAccount(@PathVariable int id, @RequestBody AccountDTO accountDTO);
}
