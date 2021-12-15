package com.disertatie.account.feign;

import com.disertatie.account.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "client-service", url = "http://localhost:8765")
public interface RoleFeignResource
{

	@RequestMapping(value = "/client-service/roles/role", params = "id")
	RoleDTO getRoleById(@RequestParam("id") int id);
}
