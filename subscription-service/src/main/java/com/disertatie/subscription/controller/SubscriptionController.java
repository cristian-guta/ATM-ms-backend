package com.disertatie.subscription.controller;

import com.disertatie.subscription.dto.ResultDTO;
import com.disertatie.subscription.dto.SubscriptionDTO;
import com.disertatie.subscription.service.SubscriptionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Data
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    //    @PreAuthorize("permitAll()")
    @GetMapping("")
    public List<SubscriptionDTO> getAllAvailableSubscriptions() {
        return subscriptionService.getAllAvailableSubs();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getSubscription")
    public SubscriptionDTO getCurrentSubscription(Principal principal) {
        return subscriptionService.getClientSubscription(principal);
    }

    @PostMapping("/createSubscription")
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
    public ResultDTO activateSubscription(Principal principal, @PathVariable(value = "id") int id) throws IOException {
        return subscriptionService.activateSubscription(principal, id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/cancelSubscription")
    public ResultDTO cancelSubscription(Principal principal) {
        return subscriptionService.cancelSubscription(principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSubscription/{id}")
    public ResultDTO deleteSubscription(@PathVariable(value = "id") int id) {
        return subscriptionService.deleteSubscription(id);
    }
}
