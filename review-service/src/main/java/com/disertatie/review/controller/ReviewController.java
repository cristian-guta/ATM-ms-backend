package com.disertatie.review.controller;

import com.disertatie.review.dto.ResultDTO;
import com.disertatie.review.dto.ReviewDTO;
import com.disertatie.review.model.Review;
import com.disertatie.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin("*")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{page}/{size}")
    public Page<ReviewDTO> getAll(@PathVariable(value = "page") int page,
                                  @PathVariable(value = "size") int size) throws IOException {
        return reviewService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public List<Review> getAllByUserId(@PathVariable(value = "id") int clientId) {
        return reviewService.getAllByClientId(clientId);
    }

    @PostMapping("/create")
    public ReviewDTO create(@RequestBody ReviewDTO review){
        return reviewService.createReview(review);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteReview(@PathVariable(value = "id") int id) {
        return reviewService.deleteReview(id);
    }

}
