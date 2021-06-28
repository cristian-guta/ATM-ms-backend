package com.disertatie.client.controller;

import com.disertatie.client.model.ClientAudit;
import com.disertatie.client.model.RevisionInfo;
import com.disertatie.client.repository.ClientAuditRepository;
import com.disertatie.client.repository.RevisionInfoRepository;
import lombok.AllArgsConstructor;
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

@RestController
@AllArgsConstructor
@RequestMapping("/audit/client")
public class ClientAuditController {

    private RevisionInfoRepository revisionInfoRepository;
    private ClientAuditRepository clientAuditRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{page}/{size}")
    public Page<ClientAudit> getAuditInfo(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        clientAuditRepository.findAll().forEach(clientAudit -> {
            RevisionInfo revisionInfo = revisionInfoRepository.findById(clientAudit.getRev());
            clientAudit.setUser(revisionInfo.getUser());
            clientAuditRepository.save(clientAudit);
        });
        Page<ClientAudit> pageResult = clientAuditRepository.findAll(pageRequest);

        List<ClientAudit> clientAudits = pageResult
                .stream()
                .collect(Collectors.toList());

        return new PageImpl<>(clientAudits, pageRequest, pageResult.getTotalElements());
    }

}
