package com.disertatie.review.feign;

import com.disertatie.review.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface ClientFeignResource {

    @RequestMapping("/client-service/clients/username/{username}")
    ClientDTO getClientByUsername(@PathVariable String username);

    @RequestMapping("/client-service/clients/email/{email}")
    ClientDTO getClientByEmail(@PathVariable String email);
}
