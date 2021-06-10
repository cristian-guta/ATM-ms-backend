package com.disertatie.subscription.dto;

import com.disertatie.subscription.model.Benefit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitDTO {
    private int id;
    private String description;
    private Benefit benefit;

    public static BenefitDTO getDto(Benefit benefit) {
        return BenefitDTO.builder()
                .id(benefit.getId())
                .description(benefit.getDescription())
                .build();
    }
}
