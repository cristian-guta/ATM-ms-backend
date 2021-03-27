package com.disertatie.account.feign;

import com.disertatie.account.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface ClientFeignResource {

    @GetMapping("/client-service/clients/username/{username}")
    ClientDTO getClientByUsername(@PathVariable String username);

    @GetMapping("/client-service/clients/{id}")
    ClientDTO getClientById(@PathVariable int id);

    @GetMapping("/client-service/clients/email/{email}")
    ClientDTO getClientByEmail(@PathVariable String email);
}
