package com.disertatie.paymentservice.feign;

import com.disertatie.paymentservice.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface RoleFeignResource {

    @RequestMapping("/client-service/roles/role/{id}")
    RoleDTO getRoleById(@PathVariable int id);
}
