package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface ClientFeignResource {

    @RequestMapping("/client-service/clients/username/{username}")
    ClientDTO getClientByUsername(@PathVariable String username);

    @RequestMapping("/client-service/clients/email/{email}")
    ClientDTO getClientByEmail(@PathVariable String email);

    @PutMapping("/client-service/clients/update/{id}")
    ClientDTO save(@PathVariable int id, @RequestBody  ClientDTO client);

    @GetMapping("/client-service/clients/findAll")
    List<ClientDTO> findAll();
}
