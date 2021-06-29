package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "client-service", url = "http://localhost:8765")
public interface ClientFeignResource {

    @GetMapping(value = "/client-service/clients/username", params = "username")
    ClientDTO getClientByUsername(@RequestParam("username") String username);

    @GetMapping(value = "/client-service/clients/email", params = "email")
    ClientDTO getClientByEmail(@RequestParam("email") String email);

    @PutMapping(value = "/client-service/clients/update", params = "id")
    ClientDTO save(@RequestParam("id") int id, @RequestBody ClientDTO client);

    @GetMapping("/client-service/clients")
    List<ClientDTO> findAll();
}
