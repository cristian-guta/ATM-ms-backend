package com.disertatie.subscription.service;

import com.disertatie.subscription.dto.AccountDTO;
import com.disertatie.subscription.dto.ClientDTO;
import com.disertatie.subscription.dto.ResultDTO;
import com.disertatie.subscription.dto.SubscriptionDTO;
import com.disertatie.subscription.feign.AccountFeignResource;
import com.disertatie.subscription.feign.ClientFeignResource;
import com.disertatie.subscription.model.Benefit;
import com.disertatie.subscription.model.Subscription;
import com.disertatie.subscription.repository.BenefitRepository;
import com.disertatie.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private BenefitRepository benefitRepository;
    private AccountFeignResource accountFeignResource;
    private ClientFeignResource clientFeignResource;

    private Logger log = Logger.getLogger(SubscriptionService.class.getName());

    public SubscriptionService(SubscriptionRepository subscriptionRepository, BenefitRepository benefitRepository,
                               AccountFeignResource accountFeignResource, ClientFeignResource clientFeignResource) {
        this.subscriptionRepository = subscriptionRepository;
        this.benefitRepository = benefitRepository;
        this.accountFeignResource = accountFeignResource;
        this.clientFeignResource = clientFeignResource;
    }

    public SubscriptionDTO createSubscription(SubscriptionDTO newSubscription) {

        List<Benefit> benefits = benefitRepository.findByIdIn(newSubscription.getBenefitIds());
        Subscription subscription = new Subscription()
                .setName(newSubscription.getName())
                .setPrice(newSubscription.getPrice())
                .setBenefits(benefits);

        return SubscriptionDTO.getDto(subscriptionRepository.save(subscription));
    }

    public SubscriptionDTO getClientSubscription(Principal principal) {
        log.info("Fetching client's subscription");

        ClientDTO client = new ClientDTO();

        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            client = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            client = clientFeignResource.getClientByUsername(principal.getName());
        }
        Subscription subscription = new Subscription();
        if (client.getSubscriptionId() != 0 && client.getUsername() != "admin") {
            subscription = subscriptionRepository.getById(client.getSubscriptionId());
            SubscriptionDTO sub = new SubscriptionDTO()
                    .setId(subscription.getId())
                    .setName(subscription.getName())
                    .setPrice(subscription.getPrice())
                    .setBenefits(subscription.getBenefits());

            return sub;
        } else {
            log.info("Something went wrong while executing getClientSubscription(...) method...");
            return new SubscriptionDTO();
        }
    }

    public List<SubscriptionDTO> getAllAvailableSubs() {

        List<SubscriptionDTO> allSubscribtions = new ArrayList<>();
        for (Subscription sub : subscriptionRepository.findAll()) {
            SubscriptionDTO subs = new SubscriptionDTO()
                    .setId(sub.getId())
                    .setName(sub.getName())
                    .setPrice(sub.getPrice())
                    .setBenefits(sub.getBenefits());
            allSubscribtions.add(subs);
        }
        return allSubscribtions;
    }

    public SubscriptionDTO getSubscriptionWithBenefits(int subId) {
        Subscription subscription = subscriptionRepository.getById(subId);
        return new SubscriptionDTO()
                .setId(subscription.getId())
                .setName(subscription.getName())
                .setPrice(subscription.getPrice())
                .setBenefits(subscription.getBenefits());
    }

    public SubscriptionDTO updateSubscription(int id, SubscriptionDTO subscriptionDTO) {
        log.info("Updating subscription...");

        List<Benefit> benefits = benefitRepository.findByIdIn(subscriptionDTO.getBenefitIds());

        Subscription updateSubscription = subscriptionRepository.getById(id);
        updateSubscription.setId(subscriptionDTO.getId())
                .setName(subscriptionDTO.getName())
                .setPrice(subscriptionDTO.getPrice())

                .setBenefits(benefits);

        log.info("Saving new subscription object state...");
        subscriptionRepository.save(updateSubscription);

        log.info("Subscription updated...");
        return SubscriptionDTO.getDto(updateSubscription);
    }

    public ResultDTO activateSubscription(int subId, Principal principal) throws IOException {
        Subscription subscription = subscriptionRepository.getById(subId);
        ClientDTO client = new ClientDTO();
        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            client = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            client = clientFeignResource.getClientByUsername(principal.getName());
        }
        log.info("Activating subscription for " + client.getUsername() + "...");

        AccountDTO account = accountFeignResource.getClientBankAccount(client.getId());
        log.info("Processing payment...");

        Double price = subscription.getPrice();
        Double amount = account.getAmount();
        if (amount < price) {
            throw new RuntimeException("Not enough funds!");
        }
        amount -= price;
        account.setAmount(amount);
        account.setCliendId(client.getId());

        accountFeignResource.updateAccount(account.getId(), account);

        log.info("Payment received...");
        log.info("Subscription activated...");

        return new ResultDTO().setStatus(true).setMessage("Subscription activated!");
    }

    public ResultDTO cancelSubscription(Principal principal) {
        ClientDTO client = new ClientDTO();
        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            client = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            client = clientFeignResource.getClientByUsername(principal.getName());
        }

        log.info("Canceling subscription for ..." + client.getFirstName() + " " + client.getLastName() + "...");
        client.setSubscriptionId(0);

        log.info("Subscription canceled...");
        return new ResultDTO().setStatus(true).setMessage("Subscription removed from your account!");
    }

    public ResultDTO deleteSubscription(int id) {

        for (ClientDTO client : clientFeignResource.findAll()) {

            if (client.getSubscriptionId() > 0 && client.getSubscriptionId() == id) {
                client.setSubscriptionId(0);
                clientFeignResource.save(client.getId(), client);
            }
        }

        subscriptionRepository.deleteSubscriptionById(id);
        return new ResultDTO().setStatus(true).setMessage("Subscription deleted.");
    }
}
