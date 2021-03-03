package com.disertatie.review.service;

import com.disertatie.review.dto.ClientDTO;
import com.disertatie.review.dto.ResultDTO;
import com.disertatie.review.dto.ReviewDTO;
import com.disertatie.review.feign.ClientFeignResource;
import com.disertatie.review.model.Review;
import com.disertatie.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    public ReviewService(ClientFeignResource clientFeignResource, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.clientFeignResource = clientFeignResource;
    }

    public List<Review> getAllByClientId(int clientId) {
        return reviewRepository.findByClientId(clientId);
    }

    public ReviewDTO createReview(ReviewDTO review) throws IOException {

        String clientUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        ClientDTO client = new ClientDTO();
        if (clientFeignResource.getClientByUsername(clientUsername) == null) {
            client = clientFeignResource.getClientByEmail(clientUsername);
        } else {
            client = clientFeignResource.getClientByUsername(clientUsername);
        }

        Review newReview = new Review()
                .setClientId(client.getId())
                .setDescription(review.getDescription())
                .setTitle(review.getTitle());

        runPythonScript();

        return new ReviewDTO(reviewRepository.save(newReview));
    }

    public void runPythonScript() throws IOException {
        System.out.println("Running python script");
        ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/egutcri/PycharmProjects/Test/main.py");
        Process p = pb.start();
    }

    public ResultDTO deleteReview(int id) {
        reviewRepository.deleteById(id);
        return new ResultDTO().setMessage("Review deleted!").setStatus(true);
    }

    public Page<ReviewDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Review> pageResult = reviewRepository.findAll(pageRequest);

        List<ReviewDTO> reviews = pageResult.
                stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(reviews, pageRequest, pageResult.getTotalElements());
    }
}
