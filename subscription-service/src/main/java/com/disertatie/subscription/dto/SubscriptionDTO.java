package com.disertatie.subscription.dto;

import com.disertatie.subscription.model.Benefit;
import com.disertatie.subscription.model.Subscription;
import com.disertatie.subscription.model.SubscriptionNetwork;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {

    private int id;
    private String name;
    private Double price;
    private List<Benefit> benefits;
    private int[] benefitIds;
    private String subscriptionNetwork;

    public static SubscriptionDTO getDto(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .price(subscription.getPrice())
                .benefits(subscription.getBenefits())
                .subscriptionNetwork(subscription.getSubscriptionNetwork())
                .build();
    }
}
