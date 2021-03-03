package com.disertatie.apigateway.dto;

import com.disertatie.apigateway.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private int id;
    private String name;
    private String description;
    private Role role;

    public RoleDTO(Role role){
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }
}
