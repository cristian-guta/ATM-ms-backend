package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "http://localhost:8765")
public interface AccountFeignResource {

    @GetMapping("/account-service/accounts/client/{id}")
    AccountDTO getClientBankAccount(@PathVariable("id") int id);

    @PutMapping("/account-service/accounts/{id}")
    AccountDTO updateAccount(@PathVariable int id, @RequestBody AccountDTO accountDTO);
}
