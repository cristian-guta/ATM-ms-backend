package com.disertatie.subscription.controller;

import com.disertatie.subscription.SubscriptionNotFoundException;
import com.disertatie.subscription.dto.ResultDTO;
import com.disertatie.subscription.dto.SubscriptionDTO;
import com.disertatie.subscription.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDTO> getAllAvailableSubscriptions(Principal principal) throws SubscriptionNotFoundException {
        return subscriptionService.getAllAvailableSubs(principal);
    }

    @GetMapping("/all")
    public List<SubscriptionDTO> getByTelephoneNumber() {
        return subscriptionService.getAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getSubscription")
    public SubscriptionDTO getCurrentSubscription(Principal principal) {
        return subscriptionService.getClientSubscription(principal);
    }

    @PostMapping
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO newSubscription) {
        return subscriptionService.createSubscription(newSubscription);
    }

    @GetMapping("/{id}")
    public SubscriptionDTO getSubscriptionWithBenefits(@PathVariable("id") int subId) {
        return subscriptionService.getSubscriptionWithBenefits(subId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateSubscription/{id}")
    public SubscriptionDTO updateSubscription(@PathVariable(value = "id") int id, @RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.updateSubscription(id, subscriptionDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/activateSubscription/{id}")
    public ResultDTO activateSubscription(@PathVariable(value = "id") int id, Principal principal) throws IOException {
        return subscriptionService.activateSubscription(id, principal);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/cancelSubscription")
    public ResultDTO cancelSubscription(Principal principal) {
        return subscriptionService.cancelSubscription(principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResultDTO deleteSubscription(@PathVariable(value = "id") int id) {
        return subscriptionService.deleteSubscription(id);
    }
}
