package com.disertatie.client.repository;

import com.disertatie.client.model.ClientRetention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRetentionRepository extends JpaRepository<ClientRetention, Integer> {

}
