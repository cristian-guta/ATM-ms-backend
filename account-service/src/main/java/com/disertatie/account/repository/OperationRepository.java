package com.disertatie.account.repository;

import com.disertatie.account.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
    Operation findOperationById(int id);

    Page<Operation> findByClientId(int id, Pageable pageable);
}

