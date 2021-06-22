package com.disertatie.review.dto;

import com.disertatie.review.model.Review;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDTO {
    private int id;
    private String title;
    private String description;
    private int clientId;

    public static ReviewDTO getDto(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .title(review.getTitle())
                .description(review.getDescription())
                .clientId(review.getClientId())
                .build();
    }
}
