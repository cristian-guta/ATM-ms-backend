package com.disertatie.subscription.feign;

import com.disertatie.subscription.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="client-service", url = "http://localhost:8765")
public interface RoleFeignResource {

    @RequestMapping("/client-service/roles/role/{id}")
    RoleDTO getRoleById(@PathVariable int id);
}
