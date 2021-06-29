package com.disertatie.paymentservice.controller;

import com.disertatie.paymentservice.model.StripeClient;
import com.stripe.model.Charge;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeClient.chargeNewCard(token, amount * 100);
    }
}
