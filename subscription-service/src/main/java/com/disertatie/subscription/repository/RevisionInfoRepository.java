package com.disertatie.subscription.repository;

import com.disertatie.subscription.model.RevisionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionInfoRepository extends JpaRepository<RevisionInfo, Integer> {
    RevisionInfo findById(int id);
}
