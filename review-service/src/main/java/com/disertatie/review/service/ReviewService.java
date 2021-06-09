package com.disertatie.review.service;

import com.disertatie.review.dto.ClientDTO;
import com.disertatie.review.dto.ResultDTO;
import com.disertatie.review.dto.ReviewDTO;
import com.disertatie.review.feign.ClientFeignResource;
import com.disertatie.review.model.Review;
import com.disertatie.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ClientFeignResource clientFeignResource;

    public ReviewService(ClientFeignResource clientFeignResource, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.clientFeignResource = clientFeignResource;
    }

    public List<ReviewDTO> getAllByClientId(int clientId) {
        return reviewRepository.findByClientId(clientId).stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    public ReviewDTO createReview(ReviewDTO review, Principal principal) {
        ClientDTO client = new ClientDTO();

        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            client = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            client = clientFeignResource.getClientByUsername(principal.getName());
        }

        Review newReview = new Review()
                .setClientId(client.getId())
                .setDescription(review.getDescription())
                .setTitle(review.getTitle());

        return new ReviewDTO(reviewRepository.save(newReview));
    }

    public void runPythonScript() throws IOException {
        String ScriptPath = "/Users/cristianguta/Desktop/ATM-BACKEND/review-service/src/main/resources/python/main.py";
        String pythonPath = "/Users/cristianguta/Desktop/ATM-BACKEND/review-service/venv/bin/python3.9";

        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, ScriptPath);
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
    }

    public ResultDTO deleteReview(int id) {
        reviewRepository.deleteById(id);
        return new ResultDTO().setMessage("Review deleted!").setStatus(true);
    }

    public Page<ReviewDTO> getAll(int page, int size) throws IOException {

        runPythonScript();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> pageResult = reviewRepository.findAll(pageRequest);

        List<ReviewDTO> reviews = pageResult.
                stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(reviews, pageRequest, pageResult.getTotalElements());
    }
}
