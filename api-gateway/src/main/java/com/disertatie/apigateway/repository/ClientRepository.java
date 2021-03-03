package com.disertatie.apigateway.repository;

import com.disertatie.apigateway.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c from Client c where c.username like ?1")
    Client findByUsername(String username);

    @Query("select c from Client c where c.id = ?1")
    Client getById(int id);

    @Query("select c from Client c where c.cnp = ?1")
    Client findClientByCnp(String cnp);

    @Query("select c from Client c where c.email = ?1")
    Client findClientByEmail(String email);
}
