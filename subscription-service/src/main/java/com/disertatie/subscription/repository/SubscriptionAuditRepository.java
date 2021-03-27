package com.disertatie.subscription.repository;

import com.disertatie.subscription.model.SubscriptionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionAuditRepository extends JpaRepository<SubscriptionAudit, Integer> {

    Page<SubscriptionAudit> findAll(Pageable pageable);
}
