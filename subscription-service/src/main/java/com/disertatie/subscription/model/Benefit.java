package com.disertatie.subscription.model;

import com.disertatie.subscription.dto.BenefitDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "benefit")
@AllArgsConstructor
@Audited
@Builder
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String description;

    public Benefit() {
    }

    public static Benefit getModel(BenefitDTO benefitDTO){
        return Benefit.builder()
                .description(benefitDTO.getDescription())
                .build();
    }
}
