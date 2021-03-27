package com.disertatie.apigateway.repository;

import com.disertatie.apigateway.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
}
