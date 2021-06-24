package com.disertatie.client.controller;

import com.disertatie.client.dto.RoleDTO;
import com.disertatie.client.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/role")
    public RoleDTO getRoleById(@RequestParam("id") int roleId) {
        return roleService.getRoleById(roleId);
    }

    @PostMapping("/create")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }
}
