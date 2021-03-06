package com.disertatie.client.repository;

import com.disertatie.client.model.ClientEmotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientEmotionRepository extends JpaRepository<ClientEmotion, Integer> {
    Page<ClientEmotion> findAll(Pageable pageable);
}
