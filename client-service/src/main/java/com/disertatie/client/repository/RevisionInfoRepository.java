package com.disertatie.client.repository;

import com.disertatie.client.model.RevisionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionInfoRepository extends JpaRepository<RevisionInfo, Integer> {
    RevisionInfo findById(int id);
}
