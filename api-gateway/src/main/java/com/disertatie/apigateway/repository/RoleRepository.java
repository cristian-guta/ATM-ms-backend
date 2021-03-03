package com.disertatie.apigateway.repository;


import com.disertatie.apigateway.dto.RoleDTO;
import com.disertatie.apigateway.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
    RoleDTO findById(int id);
}
