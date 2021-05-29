package com.disertatie.review.dto;

import com.disertatie.review.model.Review;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private int id;
    private String title;
    private String description;
    private int clientId;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.clientId = review.getClientId();
    }
}
