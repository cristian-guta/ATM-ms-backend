package com.disertatie.review.controller;

import com.disertatie.review.dto.ResultDTO;
import com.disertatie.review.dto.ReviewDTO;
import com.disertatie.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    @GetMapping("/{page}/{size}")
    public Page<ReviewDTO> getAll(@PathVariable(value = "page") int page,
                                  @PathVariable(value = "size") int size) throws IOException {
        return reviewService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public List<ReviewDTO> getAllByUserId(@PathVariable(value = "id") int clientId) {
        return reviewService.getAllByClientId(clientId);
    }

    @PostMapping
    public ReviewDTO create(@RequestBody ReviewDTO review) {
        return reviewService.createReview(review);
    }

    @DeleteMapping("/{id}")
    public ResultDTO deleteReview(@PathVariable(value = "id") int id) {
        return reviewService.deleteReview(id);
    }

}
