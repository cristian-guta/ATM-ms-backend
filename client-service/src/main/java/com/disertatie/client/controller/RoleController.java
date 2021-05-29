package com.disertatie.client.controller;

import com.disertatie.client.dto.RoleDTO;
import com.disertatie.client.model.Role;
import com.disertatie.client.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping("/getAllRoles")
    public List<RoleDTO> getAllRoles() {
        logger.info("Into getAllRoles endpoint");
        return roleService.getAllRoles();
    }

    @GetMapping("/role/{id}")
    public RoleDTO getRoleById(@PathVariable("id") int roleId) {
        return roleService.getRoleById(roleId);
    }

    @PostMapping("/role/create")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }
}
