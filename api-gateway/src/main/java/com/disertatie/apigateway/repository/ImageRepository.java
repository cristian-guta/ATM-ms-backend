package com.disertatie.apigateway.repository;

import com.disertatie.apigateway.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Integer> {
    Optional<ImageModel> findByName(String name);
}
