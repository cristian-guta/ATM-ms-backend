package com.disertatie.client.dto;

import com.disertatie.client.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private int id;
    private String name;
    private String description;
    private Role role;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }

    public RoleDTO(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
