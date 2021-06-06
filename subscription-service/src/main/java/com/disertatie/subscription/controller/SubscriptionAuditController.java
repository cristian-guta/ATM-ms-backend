package com.disertatie.subscription.controller;

import com.disertatie.subscription.model.RevisionInfo;
import com.disertatie.subscription.model.SubscriptionAudit;
import com.disertatie.subscription.repository.RevisionInfoRepository;
import com.disertatie.subscription.repository.SubscriptionAuditRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/audit/subscriptions")
public class SubscriptionAuditController {

    private SubscriptionAuditRepository subscriptionAuditRepository;
    private RevisionInfoRepository revisionInfoRepository;

    public SubscriptionAuditController(SubscriptionAuditRepository subscriptionAuditRepository, RevisionInfoRepository revisionInfoRepository) {
        this.subscriptionAuditRepository = subscriptionAuditRepository;
        this.revisionInfoRepository = revisionInfoRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAuditInfo/{page}/{size}")
    public Page<SubscriptionAudit> getAuditInfo(@PathVariable(value = "page") int page,
                                                @PathVariable(value = "size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        subscriptionAuditRepository.findAll().forEach(subscriptionAudit -> {
            RevisionInfo revisionInfo = revisionInfoRepository.findById(subscriptionAudit.getRev());
            subscriptionAudit.setUser(revisionInfo.getUser());
            subscriptionAuditRepository.save(subscriptionAudit);
        });
        Page<SubscriptionAudit> pageResult = subscriptionAuditRepository.findAll(pageRequest);

        List<SubscriptionAudit> benefitAudits = pageResult
                .stream()
                //                .map(BenefitAudit::new)
                .collect(Collectors.toList());

        return new PageImpl<>(benefitAudits, pageRequest, pageResult.getTotalElements());
    }
}
