package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service", url = "http://localhost:8765")
public interface AccountFeignResource {

    @GetMapping(value = "/account-service/accounts/client", params = "id")
    AccountDTO getClientBankAccount(@RequestParam("id") int id);

    @GetMapping(value = "/account-service/accounts/currency")
    AccountDTO getClientBankAccountByCurrency(@RequestParam("currency") String currency, @RequestParam("username") String username);

    @PutMapping("/account-service/accounts/{id}")
    AccountDTO updateAccount(@PathVariable int id, @RequestBody AccountDTO accountDTO);
}
