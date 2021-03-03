package com.disertatie.subscription.dto;

import com.disertatie.subscription.model.Benefit;
import com.disertatie.subscription.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    private int id;
    private String name;
    private Double price;
    private List<Benefit> benefits;
    private int[] benefitIds;

    public SubscriptionDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.price = subscription.getPrice();
        this.benefits = subscription.getBenefits();
    }
}
