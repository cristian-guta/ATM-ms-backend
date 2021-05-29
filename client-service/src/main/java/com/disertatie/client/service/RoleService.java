package com.disertatie.client.service;

import com.disertatie.client.dto.RoleDTO;
import com.disertatie.client.model.Role;
import com.disertatie.client.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roles = new ArrayList<>();
        roleRepository.findAll().forEach(role -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roles.add(roleDTO);
        });
        return roles.stream().collect(Collectors.toList());
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role()
                .setName(roleDTO.getName())
                .setDescription(roleDTO.getDescription());

        return new RoleDTO(roleRepository.save(role));
    }

    public RoleDTO getRoleById(int roleId) {
        return new RoleDTO(roleRepository.findById(roleId));
    }
}
