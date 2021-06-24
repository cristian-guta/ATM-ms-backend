package com.disertatie.subscription.service;

import com.disertatie.subscription.dto.BenefitDTO;
import com.disertatie.subscription.dto.ClientDTO;
import com.disertatie.subscription.dto.ResultDTO;
import com.disertatie.subscription.feign.ClientFeignResource;
import com.disertatie.subscription.model.Benefit;
import com.disertatie.subscription.model.Subscription;
import com.disertatie.subscription.repository.BenefitRepository;
import com.disertatie.subscription.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BenefitService {

    private BenefitRepository benefitRepository;
    private SubscriptionRepository subscriptionRepository;
    private ClientFeignResource clientFeignResource;

    public BenefitService(BenefitRepository benefitRepository, SubscriptionRepository subscriptionRepository,
                          ClientFeignResource clientFeignResource) {
        this.benefitRepository = benefitRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.clientFeignResource = clientFeignResource;
    }

    public List<BenefitDTO> getAllBenefits() {
        List<BenefitDTO> benefits = new ArrayList<>();
        for (Benefit ben : benefitRepository.findAll()) {
            benefits.add(BenefitDTO.getDto(ben));
        }
        return benefits;
    }

    public Page<BenefitDTO> getAllBenefitsPaged(int page, int size) {

        log.info("Listing ALL benefits...");
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Benefit> pageResult = benefitRepository.findAll(pageRequest);
        List<BenefitDTO> benefits = pageResult
                .stream()
                .map(BenefitDTO::getDto)
                .collect(Collectors.toList());
        return new PageImpl<>(benefits, pageRequest, pageResult.getTotalElements());
    }

    public Page<BenefitDTO> getAllUSerBenefitsPaged(int page, int size, Principal principal) {
        PageRequest pageRequest = PageRequest.of(page, size);

        ClientDTO client = new ClientDTO();

        if (clientFeignResource.getClientByUsername(principal.getName()) == null) {
            client = clientFeignResource.getClientByEmail(principal.getName());
        } else {
            client = clientFeignResource.getClientByUsername(principal.getName());
        }
        Subscription subscription = subscriptionRepository.getById(client.getSubscriptionId());
        Page<Benefit> pageResult = benefitRepository.findPagedBySubId(subscription.getId(), pageRequest);
        List<BenefitDTO> benefits = pageResult
                .stream()
                .map(BenefitDTO::getDto)
                .collect(Collectors.toList());
        return new PageImpl<>(benefits, pageRequest, pageResult.getTotalElements());
    }

    public List<BenefitDTO> getBenefitsBySubscription(int id) {

        log.info("Listing all benefits by subscription...");

        List<BenefitDTO> benefits = new ArrayList<>();
        benefitRepository.findBySubscriptionId(id).forEach(benefit -> {
            benefits.add(BenefitDTO.getDto(benefit));
        });
        return benefits.stream().distinct().collect(Collectors.toList());
    }

    public BenefitDTO createBenefit(BenefitDTO benefitDTO) {
        Benefit benefit = Benefit.getModel(benefitDTO);

        return BenefitDTO.getDto(benefitRepository.save(benefit));
    }

    public ResultDTO deleteBenefit(int id) {
        benefitRepository.deleteById(id);
        return new ResultDTO().setStatus(true).setMessage("Benefit deleted.");
    }
}