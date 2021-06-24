package com.disertatie.paymentservice.feign;

import com.disertatie.paymentservice.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface ClientFeignResource {

    @GetMapping(value = "/client-service/clients/username", params = "username")
    ClientDTO getClientByUsername(@RequestParam("username") String username);

    @GetMapping(value = "/client-service/clients/email", params = "email")
    ClientDTO getClientByEmail(@RequestParam("email") String email);
}
