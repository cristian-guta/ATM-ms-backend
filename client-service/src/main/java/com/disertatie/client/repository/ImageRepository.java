package com.disertatie.client.repository;

import com.disertatie.client.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Integer> {
    Optional<ImageModel> findByName(String name);
}
