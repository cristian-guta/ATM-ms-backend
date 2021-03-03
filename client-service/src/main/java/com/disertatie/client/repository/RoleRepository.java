package com.disertatie.client.repository;

import com.disertatie.client.dto.RoleDTO;
import com.disertatie.client.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
    Role findById(int id);
}
